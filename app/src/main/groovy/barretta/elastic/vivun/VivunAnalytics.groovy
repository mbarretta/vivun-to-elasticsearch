package barretta.elastic.vivun

import barretta.elastic.vivun.objects.Activity
import barretta.elastic.vivun.objects.Deliverable
import barretta.elastic.vivun.objects.Opportunity
import com.barretta.elastic.clients.ESClient
import groovy.util.logging.Slf4j

@Slf4j
class VivunAnalytics {
    static def run(Map config, List<Activity> activities, List<Opportunity> opportunities, List<Deliverable> deliverables) {
        def oppUpdates = doSAToOpportunityAnalytics(config, activities, opportunities)
        def deliverableUpdates = doActivitiesToDeliverables(config, activities, deliverables)
        def activityUpdates = doOpportunitiesToActivities(config, opportunities, activities)

        //this will need to expand to merge inserts/deletes if enrich methods create those
        def returnBulkData = [:]
        returnBulkData[ESClient.BulkOps.UPDATE] = oppUpdates[ESClient.BulkOps.UPDATE] +
            deliverableUpdates[ESClient.BulkOps.UPDATE] +
            activityUpdates[ESClient.BulkOps.UPDATE]
        return returnBulkData
    }

    static def doOpportunitiesToActivities(Map config, List<Opportunity> opportunities, List<Activity> activities) {
        log.info("joining opportunities to activities")
        def updates = activities.groupBy { it.opportunityAccountHash }.inject([]) { list, group ->
            def matchingOpp = opportunities.find { opp -> group.key == opp.opportunityAccountHash }
            if (matchingOpp) {
                def totalOppHours = group.value.sum { it.hours } as float
                group.value.each {
                    def hoursPercentage = it.hours / totalOppHours
                    list << [
                        _id   : it.heroActivityNumber,
                        _index: config.es.indices.activities,
                        enrich: [
                            hours_opp_percentage : hoursPercentage.round(2),
                            opp_id               : matchingOpp.opportunityId,
                            opp_amount           : matchingOpp?.amount ?: 0,
                            opp_neAmount         : matchingOpp?.newAndUpsellAmount ?: 0,
                            opp_amount_adjusted  : ((matchingOpp?.amount ?: 0.0) * hoursPercentage).round(2),
                            opp_neAmount_adjusted: ((matchingOpp?.newAndUpsellAmount ?: 0.0) * hoursPercentage).round(2)
                        ]
                    ]
                }
            }
            return list
        }
        return [(ESClient.BulkOps.UPDATE): updates]
    }

    static def doActivitiesToDeliverables(Map config, List<Activity> activities, List<Deliverable> deliverables) {
        log.info("joining activities to deliverables")

        def updates = deliverables.inject([:].withDefault { [] }) { updateMap, deliverable ->
            def matchingActivities = activities.findAll {
                it.deliverable == deliverable.name && it.account == deliverable.account
            }

            def totalActivityCount = matchingActivities.size()

            if (totalActivityCount > 0) {
                def saActivities = matchingActivities.groupBy { it.createdBy }
                def saActivitiesEnriched = saActivities.inject([]) { list, rawSaActivity ->
                    rawSaActivity.each {
                        def percentage = it.value.size() / totalActivityCount
                        list << [
                            name               : it.key,
                            activity_count     : it.value.size(),
                            activity_percentage: percentage
                        ]
                    }
                    return list
                }

                //create deliverable update record w/ activities info
                updateMap[ESClient.BulkOps.UPDATE] << [
                    _id   : deliverable.deliverableNumber, // our key
                    _index: config.es.indices.deliverables,
                    enrich: [
                        sa_count      : saActivitiesEnriched.size(),
                        activity_count: totalActivityCount,
                        sa_activities : saActivitiesEnriched
                    ]
                ]
            }
            return updateMap
        }
        return updates
    }

    /**
     * add SA info to opportunity records by way of activities
     */
    static def doSAToOpportunityAnalytics(Map config, List<Activity> activities, List<Opportunity> opportunities) {
        log.info("joining activities to opportunities")
        def usedHashes = []
        //sort opps by descending date so we're most likely to get the "right" one since old opps have the same name/account combos in many cases
        opportunities.sort{a, b -> (a <=> b) }
        def updates = opportunities.inject([:].withDefault { [] }) { updateMap, opportunity ->
            if (!usedHashes.contains(opportunity.opportunityAccountHash)) {
                usedHashes << opportunity.opportunityAccountHash
                def matchingActivities = activities.findAll { it.opportunityAccountHash == opportunity.opportunityAccountHash }

                //we only want to do this if we have new activities for a new or existing opp OR we have an updated opp that needs to be re-joined to old activity
                if (matchingActivities.size() > 0 || opportunity.newUpdate) {

                    //if we only have a subset of data, fetch potential existing records from ES
                    if (config.fetchSince) {
                        //get any existing activities for this opportunity from ES so we don't update the opp record without that info
                        def existingActivities = ESClientUtils.fetchActivitiesByOpportunity(config.es.client, config.es.indices.activities, opportunity.opportunityAccountHash)

                        //if we have no matching activities in this batch, just use all the existing ones from ES (might be none)
                        //else merge new activities w/ existing such that new overrides exist
                        if (matchingActivities.size() == 0) {
                            matchingActivities = existingActivities
                        } else {
                            def vivunActivityIds = matchingActivities.collect { it.heroActivityNumber }
                            existingActivities.removeAll { vivunActivityIds.contains(it.heroActivityNumber) }
                            matchingActivities += existingActivities
                        }
                    }

                    if (matchingActivities.size() > 0) {

                        //get total activity count to allow us to calculate fractional SA contribution and money
                        def totalActivityCount = matchingActivities.size()
                        def totalActivityHours = matchingActivities.sum { it.hours }

                        //isolate SAs
                        def saActivities = matchingActivities.groupBy { it.createdBy }

                        def saActivitiesEnriched = saActivities.inject([]) { list, rawSaActivities ->
                            rawSaActivities.each {
                                def percentage = it.value.size() / totalActivityCount
                                list << [
                                    name                  : it.key,
                                    activity_count        : it.value.size(),
                                    activity_hours        : it.value.sum { it.hours },
                                    activity_percentage   : percentage,
                                    adjusted_amount       : percentage * opportunity.amount,
                                    adjusted_new_expansion: percentage * opportunity.newAndUpsellAmount
                                ]
                            }
                            return list
                        }

                        //create opportunity update record w/ SA field
                        updateMap[ESClient.BulkOps.UPDATE] << [
                            _id     : opportunity.opportunityId, // our key
                            _index  : config.es.indices.opportunities,
                            enrich  : [
                                sa_count      : saActivitiesEnriched.size(),
                                activity_count: totalActivityCount,
                                activity_hours: totalActivityHours,
                                sa_detail     : saActivitiesEnriched
                            ],
                            sa_names: saActivities.keySet()
                        ]
                    }
                }
            }
            return updateMap
        }
        return updates
    }
}
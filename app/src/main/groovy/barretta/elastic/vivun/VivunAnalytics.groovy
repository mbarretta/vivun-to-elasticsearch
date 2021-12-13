package barretta.elastic.vivun

import barretta.elastic.vivun.objects.Activity
import barretta.elastic.vivun.objects.Deliverable
import barretta.elastic.vivun.objects.Opportunity
import com.barretta.elastic.clients.ESClient
import groovy.util.logging.Slf4j

@Slf4j
class VivunAnalytics {
    static def run(Map config, List<Activity> activities, List<Opportunity> opportunities, List<Deliverable> deliverables) {
        def opptyUpdates = doSAToOpportunityAnalytics(config, activities, opportunities)
        def deliverableUpdates = doActivitiesToDeliverables(config, activities, deliverables)

        //this will need to expand to merge inserts/deletes if enrich methods create those
        def returnBulkData = [:]
        returnBulkData[ESClient.BulkOps.UPDATE] = opptyUpdates[ESClient.BulkOps.UPDATE] + deliverableUpdates[ESClient.BulkOps.UPDATE]
        return returnBulkData
    }

    static def doActivitiesToDeliverables(Map config, List<Activity> activities, List<Deliverable> deliverables) {
        log.info("joining SA activities to deliverables")

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
        log.info("joining SA activities to opportunities")
        def updates = opportunities.inject([:].withDefault { [] }) { updateMap, opportunity ->
            def matchingActivities = activities.findAll {
                it.opportunity == opportunity.opportunityName && it.account == opportunity.account
            }

            //get total activity count to allow us to calculate fractional SA contribution and money
            def totalActivityCount = matchingActivities.size()

            if (totalActivityCount > 0) {
                //isolate SAs
                def saActivities = matchingActivities.groupBy { it.createdBy }

                def saActivitiesEnriched = saActivities.inject([]) { list, rawSaActivities ->
                    rawSaActivities.each {
                        def percentage = it.value.size() / totalActivityCount
                        list << [
                            name                  : it.key,
                            activity_count        : it.value.size(),
                            activity_percentage   : percentage,
                            adjusted_amount       : percentage * opportunity.amount,
                            adjusted_new_expansion: percentage * opportunity.newAndUpsellAmount
                        ]
                    }
                    return list
                }

                //create opportunity update record w/ SA field
                updateMap[ESClient.BulkOps.UPDATE] << [
                    _id   : opportunity.opportunityId, // our key
                    _index: config.es.indices.opportunities,
                    enrich: [
                        sa_count      : saActivitiesEnriched.size(),
                        activity_count: totalActivityCount,
                        sa_detail     : saActivitiesEnriched
                    ],
                    sa_names: saActivities.keySet()
                ]
            }
            return updateMap
        }
        return updates
    }
}
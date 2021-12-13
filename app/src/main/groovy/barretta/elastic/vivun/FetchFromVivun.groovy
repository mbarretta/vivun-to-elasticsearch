package barretta.elastic.vivun

import barretta.elastic.vivun.objects.Activity
import barretta.elastic.vivun.objects.Deliverable
import barretta.elastic.vivun.objects.Opportunity
import com.opencsv.CSVReader
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

@Slf4j
class FetchFromVivun {
    final static String ENDPOINT_DELIVERABLES = "DELIVERABLE"
    final static String ENDPOINT_OPPORTUNITIES = "opportunity/users?fetch_using_user_roles=false"
    final static String ENDPOINT_ACTIVITIES = "HERO_ACTIVITY"

    public static def activities(config) {
        def requestObj = new JsonBuilder([
            fields          : [
                "OwnerId", "vh__Account__c", "vh__Opportunity__c", "vh__Type__c", "vh__Description__c", "vh__Date__c", "vh__Hours__c", "vh__Deliverable__c", "IsDeleted", "CreatedDate", "CreatedById", "LastModifiedDate", "LastModifiedById", "LastViewedDate", "LastReferencedDate", "vh__Created_Stage__c", "vh__Default_Hours__c", "vh__Focus__c", "vh__Hero_Activity_Flag__c", "vh__Presales_Stage__c", "vh__Source__c", "vh__Status__c", "Name"
            ],
            labels          : {},
            query           : '[{"name":"CreatedBy.Name","operator":"in","value":["' + config.names.join('","') + '"]}'+buildDateQuery(config)+']',
            export_type     : "csv",
            default_currency: "USD",
            zone_id         : "UTC"
        ]).toString()

        return doFetch(config, ENDPOINT_ACTIVITIES, requestObj, Activity.class)
    }

    public static def deliverables(config) {
        def requestObj = new JsonBuilder([
            fields          : [
                "Name", "vh__Name__c", "vh__Status__c", "vh__Type__c", "vh__Hours__c", "vh__Scheduled_Date__c", "vh__Account__c", "vh__Opportunity__c", "Id", "OwnerId", "IsDeleted", "CurrencyIsoCode", "CreatedDate", "CreatedById", "LastModifiedDate", "LastModifiedById", "SystemModstamp", "LastViewedDate", "LastReferencedDate", "vh__Completed_Date__c", "vh__Completed_Presales_Stage__c", "vh__Completed_Stage__c", "vh__Deliverable_Flag__c", "vh__Description__c", "vh__Due_Date__c", "vh__Parent_TMR__c", "vh__Scheduled_To_Completed__c", "vh__Started_Date__c", "vh__Started_To_Completed__c", "Deliverable_Created_By_Workday_ID__c", "Deliverable_Owner_Workday_ID__c"
            ],
            labels          : {},
            query           : '[{"name":"Owner.Name","operator":"in","value":["' + config.names.join('","') + '"]}' + buildDateQuery(config) + ']',
            export_type     : "csv",
            default_currency: "USD",
            zone_id         : "UTC"
        ]).toString()

        return doFetch(config, ENDPOINT_DELIVERABLES, requestObj, Deliverable.class)
    }

    public static def opportunities(config) {
        //might notice we don't add date filter to this. The reason is that activities can be added to opps that weren't recently created
        //or updated. If we don't pull them all in each time, we can't do the enrichment/joins that we do in [VivunAnalytics]
        def requestObj = new JsonBuilder([
            fields          : [
                "Name", "AccountId", "OwnerId", "CloseDate", "StageName", "ForecastCategoryName", "vh__Opportunity_Score__c", "Id", "IsDeleted", "RecordTypeId", "IsPrivate", "Description", "Amount", "Probability", "ExpectedRevenue", "Type", "NextStep", "LeadSource", "IsClosed", "IsWon", "CurrencyIsoCode", "CampaignId", "HasOpportunityLineItem", "IsSplit", "Pricebook2Id", "Territory2Id", "IsExcludedFromTerritory2Filter", "CreatedDate", "CreatedById", "LastModifiedDate", "LastModifiedById", "SystemModstamp", "LastActivityDate", "FiscalQuarter", "FiscalYear", "Fiscal", "LastViewedDate", "LastReferencedDate", "HasOpenActivity", "HasOverdueTask", "Balance__c", "ContractAmount_1__c", "vh__Can_Revive__c", "Total_Discount_Amount__c", "vh__Date_Revived__c", "ACV_Upsell_Attrition__c", "TCV_Amount__c", "Greatest_Opp_Line_Amount__c", "ARR_Amount__c", "Net_Amount__c", "vh__Hero_Opportunity_Flag__c", "vh__Presales_Concern__c", "vh__Presales_Stage__c", "vh__Technical_Differentiation__c", "New_Upsell_Amount__c", "Base_Renewal_Amount__c", "Expansion_ACV__c", "Renewal_MY__c", "Renewed_Subscription_Amount__c", "Total_Partner_Uplift_Amount__c", "RM_Forecast__c", "POAmt_01__c", "POAmt_02__c", "POAmt_03__c", "POAmt_04__c", "POAmt_05__c", "POAmtSum__c", "LID__My_Geolocation__Latitude__s", "LID__My_Geolocation__Longitude__s", "SA_Manager_Qualification__c", "SA_Manager_Comments__c"
            ],
            labels          : ["Name": "Opportunity Name"],
            query           : '[{"name":"team_id","operator":"in","value":["' + config.teams.join('","') + '"]}]',
            export_type     : "csv",
            default_currency: "USD",
            zone_id         : "UTC"
        ]).toString()

        return doFetch(config, ENDPOINT_OPPORTUNITIES, requestObj, Opportunity.class)
    }

    private static List<?> doFetch(Map config, String endpoint, String requestObj, Class objClass) {
        def conn = setupConn(config, endpoint)
        conn.outputStream.write(requestObj.bytes)

        //the above request will return us the url - isolate it
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.inputStream, "utf-8"))
        def csvUrl = new JsonSlurper().parse(reader).url
        conn.disconnect()

        //fetch the csv from the above isolated url
        def csvConn = new URL(csvUrl).openConnection() as HttpURLConnection
        def csv = fetchCsvResponse(csvConn, objClass)
        log.info("fetched [${csv.size()}] records")
        csvConn.disconnect()

        return csv
    }

    private static List<Object> fetchCsvResponse(HttpURLConnection conn, Class objClass)
    {
        def data = []
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(conn.inputStream, "utf-8"))
            reader.skip(1) //skip header

            data = reader.readAll().collect { objClass.getConstructor(String[].class).newInstance(new Object[] {it}) }

        } catch (IOException e) {
            log.error("doh", e)
        }
        return data
    }

    private static HttpURLConnection setupConn(config, endpoint) {
        assert config.vivunUrl != null, "vivunUrl must be set in the settings file"

        def conn = new URL(config.vivunUrl + endpoint).openConnection() as HttpURLConnection
        conn.setRequestMethod("POST")
        conn.setDoOutput(true)
        conn.setRequestProperty("Authorization", "Bearer $config.authToken")
        conn.setRequestProperty("Content-Type", "application/json; utf-8")
        conn.setRequestProperty("Accept", "application/json")
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36")

        return conn
    }

    private static def buildDateQuery(config) {
        def dateQuery = ""
        if (config.fetchSince) {
           dateQuery = ',{"name":"CreatedDate","operator":"gt","value":"'+config.fetchSince+'T00:00:00.000Z"}'+
               ',{"name":"LastModifiedDate","operator":"gt","value":"'+config.fetchSince+'T00:00:00.000Z"}'
        }
        return dateQuery
    }
}

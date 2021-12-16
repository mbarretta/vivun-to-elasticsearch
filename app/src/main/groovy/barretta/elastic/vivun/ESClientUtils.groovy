package barretta.elastic.vivun

import barretta.elastic.vivun.objects.Opportunity
import barretta.elastic.vivun.objects.VivunObject
import com.barretta.elastic.clients.ESClient
import groovy.util.logging.Slf4j
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.enrich.ExecutePolicyRequest
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.SearchHit

@Slf4j
class ESClientUtils {
    static def bulkInsertCsv(Map esClientConfig, List csv, String index) {
        def esClient = new ESClient(esClientConfig as ESClient.Config)

        //build up obj with `message`=<csv row>
        def inserts = csv.inject([]) { list, item ->
            list << [message: item.toCsv()]
        }

        log.debug("Bulk inserting to index [$index] with config: $esClient.config")
        esClient.bulkInsert(inserts, index)
    }

    static def bulkUpdate(Map esClientConfig, List<VivunObject> records, String index) {
        def esClient = new ESClient(esClientConfig as ESClient.Config)

        log.debug("Bulk updated to index [$index] with config: $esClient.config")
        esClient.bulk( [(ESClient.BulkOps.UPDATE): records.collect { it.toMap() }], index )
    }

    static def bulk(Map esClientConfig, Map<ESClient.BulkOps, List<Map>> records) {
        def esClient = new ESClient(esClientConfig as ESClient.Config)

        log.debug("Bulk loading data with config: $esClient.config")
        esClient.bulk(records)
    }

    static def reExecuteEnrichPolicy(Map esClientConfig, String policy) {
        def esClient = new ESClient(esClientConfig as ESClient.Config)
        log.info("refreshing enrichment policy [$policy]")
        return esClient.enrich().executePolicy(new ExecutePolicyRequest(policy), RequestOptions.DEFAULT)
    }

    static List<Opportunity> fetchOpportunities(Map esClientConfig, String index) {
        def config = esClientConfig as ESClient.Config
        config.index = index
        def esClient = new ESClient(config)
        log.info("fetching existing opportunities from ES")
        def opps = []
        esClient.scrollQuery(QueryBuilders.matchAllQuery(), 100, { opps << new Opportunity(it as SearchHit) })
        return opps
    }
}

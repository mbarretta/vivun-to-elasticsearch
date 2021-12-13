package barretta.elastic.vivun

import com.barretta.elastic.clients.ESClient
import groovy.util.logging.Slf4j
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.enrich.ExecutePolicyRequest

@Slf4j
class SendToES {
    public static def bulkInsert(Map esClientConfig, List csv, String index) {
        def esClient = new ESClient(esClientConfig as ESClient.Config)

        //drop the header row and build up obj with `message`=<csv row>
        def inserts = csv.inject([]) { list, item ->
            list << [message: item.toCsv()]
        }

        log.debug("Bulk inserting to index [$index] with config: $esClient.config")
        def task = esClient.bulkInsert(inserts, index)
    }

    public static def bulk(Map esClientConfig, Map<ESClient.BulkOps, List<Map>> records) {
        def esClient = new ESClient(esClientConfig as ESClient.Config)

        log.debug("Bulk loading data with config: $esClient.config")
        esClient.bulk(records)
    }

    public static def reExecuteEnrichPolicy(Map esClientConfig, String policy) {
        def esClient = new ESClient(esClientConfig as ESClient.Config)
        log.info("refreshing enrichment policy [$policy]")
        return esClient.enrich().executePolicy(new ExecutePolicyRequest(policy), RequestOptions.DEFAULT)
    }

}


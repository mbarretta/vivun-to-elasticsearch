package barretta.elastic.vivun


import com.barretta.elastic.clients.ESClient
import groovy.yaml.YamlSlurper
import spock.lang.Specification

class ESClientUtilsSpec extends Specification {
    def csv
    def config
    ESClient esClient

    void setup() {
        csv = ['"header 1","header 2","header 3"','"cell 1","cell 2","cell 3"']
//        csv = ["asdf","asdf"]
        config = new YamlSlurper().parse(GroovyClassLoader.getSystemResource('test-settings.yml').openStream())
        esClient = new ESClient(config.es.client as ESClient.Config)
    }

    void cleanup() {

    }
    def "fetching opportunities works"() {
       when:
       def opps = ESClientUtils.fetchOpportunities(config, config.es.indices.opportunities)

    }

    def "Send"() {
        when:
        ESClientUtils.bulkInsertCsv(config, csv, "test")

        then:
        assert true
    }
}

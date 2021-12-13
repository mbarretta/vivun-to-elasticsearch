package barretta.elastic

import barretta.elastic.vivun.SendToES
import com.barretta.elastic.clients.ESClient
import groovy.yaml.YamlSlurper
import spock.lang.Specification

class SendToESTest extends Specification {
    def csv
    def config
    ESClient esClient

    void setup() {
        csv = ['"header 1","header 2","header 3"','"cell 1","cell 2","cell 3"']
//        csv = ["asdf","asdf"]
        config = new YamlSlurper().parse(GroovyClassLoader.getSystemResource('test-settings.yml').openStream())
        esClient = new ESClient(config.es as ESClient.Config)
    }

    void cleanup() {

    }

    def "Send"() {
        when:
        SendToES.bulkInsert(config, csv, "test")

        then:
        assert true
    }
}

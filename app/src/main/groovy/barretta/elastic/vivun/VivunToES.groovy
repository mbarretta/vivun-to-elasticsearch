package barretta.elastic.vivun

import groovy.cli.picocli.CliBuilder
import groovy.util.logging.Slf4j
import groovy.yaml.YamlSlurper
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml

import java.time.LocalDate

@Slf4j
class VivunToES {
    final static String PROPERTIES_FILE = "settings.yml"
    static def config

    final static void main(String[] args) {
        loadConfig()

        def cli = new CliBuilder(usage: "vivunToES [options]", header: "Options:")
        cli.authToken("Authentication bearer token to pass to Vivun", args: 1, argName: "token")
        cli.h(longOpt: "help", "Display Usage")

        def options = cli.parse(args)
        if (options && args) {
            if (options.h) {
                cli.usage()
                System.exit(0)
            } else if (options.authToken) {
                config.authToken = options.authToken
            }
        }
        if (!config.authToken) {
            log.error("authToken must be set in $PROPERTIES_FILE or passed in via CLI")
            System.exit(1)
        }
        run()
    }

    private static def loadConfig() {
        if (new File(PROPERTIES_FILE).exists()) {
            config = new YamlSlurper().parse(new File(PROPERTIES_FILE))
            log.info("loaded config")
        } else {
            throw new RuntimeException("unable to find $PROPERTIES_FILE")
        }
    }

    private static def run() {
        if (config.fetchSince) {
            log.info("fetching data since [$config.fetchSince]")
        }

        log.info("fetching deliverables...")
        def deliverables = FetchFromVivun.deliverables(config)
        SendToES.bulkInsert(config.es.client, deliverables, config.es.indices.deliverables)
        SendToES.reExecuteEnrichPolicy(config.es.client, config.es.enrichPolicies.deliverables)
        log.debug("done")

        log.info("fetching opportunities...")
        def opportunities = FetchFromVivun.opportunities(config)
        SendToES.bulkInsert(config.es.client, opportunities, config.es.indices.opportunities)
        SendToES.reExecuteEnrichPolicy(config.es.client, config.es.enrichPolicies.opportunities)
        log.debug("done")

        log.info("fetching activities...")
        def activities = FetchFromVivun.activities(config)
        SendToES.bulkInsert(config.es.client, activities, config.es.indices.activities)
        log.debug("done")

        log.info("running additional analytics...")
        def additionalAnalytics = VivunAnalytics.run(config, activities, opportunities, deliverables)
        SendToES.bulk(config.es.client, additionalAnalytics)
        log.debug("done")

        config.fetchSince = LocalDate.now().format("yyyy-MM-dd")
        saveConfig(config as ConfigObject)

        Thread.sleep(10000) // seems needed to prevent exit before the async ES bulk load finishes
        System.exit(0)
    }

    private static def saveConfig(ConfigObject config) {
        def dumper = new DumperOptions()
        dumper.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK)
        dumper.setPrettyFlow(true)
        new File(PROPERTIES_FILE).withWriter {
            it.write(new Yaml(dumper).dump(config))
        }
    }
}

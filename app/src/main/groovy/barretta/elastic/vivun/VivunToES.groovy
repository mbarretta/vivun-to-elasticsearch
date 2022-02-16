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
        ESClientUtils.bulkInsertCsv(config.es.client, deliverables, config.es.indices.deliverables)
        ESClientUtils.reExecuteEnrichPolicy(config.es.client, config.es.enrichPolicies.deliverables)
        log.debug("done")

        log.info("fetching opportunities...")
        def allOpportunities = runOpportunityFetchLogic()
        ESClientUtils.reExecuteEnrichPolicy(config.es.client, config.es.enrichPolicies.opportunities)
        log.debug("done")

        log.info("fetching activities...")
        def activities = FetchFromVivun.activities(config)
        ESClientUtils.bulkInsertCsv(config.es.client, activities, config.es.indices.activities)
        log.debug("done")

        log.info("running additional analytics...")
        def additionalAnalytics = VivunAnalytics.run(config, activities, allOpportunities, deliverables)
        ESClientUtils.bulk(config.es.client, additionalAnalytics)
        log.debug("done")

        config.fetchSince = LocalDate.now().format("yyyy-MM-dd")
        saveConfig(config as ConfigObject)

        Thread.sleep(10000) // seems needed to prevent exit before the async ES bulk load finishes
        System.exit(0)
    }

    private static def runOpportunityFetchLogic() {
        //somewhat complex dance to commence that will fetch all opps from ES and add in the newly created/updated opps from Vivun
        def allOpportunities = []

        //grab new and updated opps from Vivun, wait to save to ES until after the next step
        def vivunOpportunities = FetchFromVivun.opportunities(config)
        allOpportunities += vivunOpportunities

        //if we did a fetchSince, fetch "the rest" from ES and remove those contained within the vivun batch
        //this is needed so we have a full set of opps to use for downstream analytics
        if (config.fetchSince) {
            def existingOpportunities = ESClientUtils.fetchOpportunities(config.es.client, config.es.indices.opportunities)

            //remove the matching ES opp record so that the resulting "all" list won't have the old data
            def updatedIds = vivunOpportunities.collect { it.opportunityId }
            existingOpportunities.removeAll { updatedIds.contains(it.opportunityId) }

            allOpportunities += existingOpportunities
        }
        //now we can save the new/updated vivun opps since we've pulled and used the "old" existing ES data
        ESClientUtils.bulkInsertCsv(config.es.client, vivunOpportunities, config.es.indices.opportunities)

        return allOpportunities
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

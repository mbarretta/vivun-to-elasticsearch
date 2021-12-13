package barretta.elastic

import barretta.elastic.vivun.FetchFromVivun
import groovy.yaml.YamlSlurper
import spock.lang.Specification

class FetchFromVivunTest extends Specification {
    def config

    void setup() {
        config = new YamlSlurper().parse(GroovyClassLoader.getSystemResource('test-settings.yml').openStream())
        config.authToken = "eyJraWQiOiJGZm01NjI4R1RBbnNQc1RHOXpwaDJMWVpFNmZwK0VyalB5NFVJMG9BOUJNPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI5ZmU1MzE1Ni01ZGE3LTQ2MmItOGUyNS02ZDI3ZTYwMzkxYjQiLCJldmVudF9pZCI6IjE4MDA0OWJmLWYxNjAtNDMxOS1hYzk0LTkzNjIxZDVkZjNjNyIsInRva2VuX3VzZSI6ImFjY2VzcyIsInNjb3BlIjoiYXdzLmNvZ25pdG8uc2lnbmluLnVzZXIuYWRtaW4iLCJhdXRoX3RpbWUiOjE2MzcyNDQ2MTEsImlzcyI6Imh0dHBzOlwvXC9jb2duaXRvLWlkcC51cy13ZXN0LTIuYW1hem9uYXdzLmNvbVwvdXMtd2VzdC0yX2VJZ0wyRTF6biIsImV4cCI6MTYzNzMzMTAxMSwiaWF0IjoxNjM3MjQ0NjExLCJqdGkiOiJiMGUzYWVmNS1kOGJmLTQ5OWMtOGI0Mi01ODY2ZjI1YzcxM2EiLCJjbGllbnRfaWQiOiI3bmZoZGY2bDM0NnN0MzZlZXBzdWJyZG1qcyIsInVzZXJuYW1lIjoiOWZlNTMxNTYtNWRhNy00NjJiLThlMjUtNmQyN2U2MDM5MWI0In0.E2ljw1_UfXdNl78kXQlBkVGcsjc22fc4YyrQOBshHC_NcGaXoXjMMURhUKLLgt7uW0qFn5dWmAIZR5HSEC1dJxu4xuG27JfyXi2IcU1fDJ_JhfXR35F0vV3tr9Yh5fsBMW_xawjNmje-kriWXXxYBVwmiwwM-kyek0A98AT_0b7QHUn-o2ZUZP97ZYi6FOxMWGhpF5dGUn4ilVgn_dhCdgS-KlVZRX_11JWhEZGKV7zZhCF18JK-0G2Lwe-n3Yan_AWg-bjIak-mCUSKYc2uOp5WSQ97K516-b0DuB9ZFrCWKrbY-clcJ-Ueyqxni4tkLt-1bdovz5RTk-M9xAPCaw"
    }

    def "Activities"() {
    }

    def "Deliverables"() {
        when:
        def csv = FetchFromVivun.deliverables(config)

        then:
        assert csv.size() > 0

    }
}

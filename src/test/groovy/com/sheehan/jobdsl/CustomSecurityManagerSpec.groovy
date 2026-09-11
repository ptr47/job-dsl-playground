package com.sheehan.jobdsl

import spock.lang.Specification
import spock.lang.IgnoreIf

@IgnoreIf({ System.getProperty('java.specification.version').toBigDecimal() >= 18 })
class CustomSecurityManagerSpec extends Specification {

    def setup() {
        System.securityManager = new CustomSecurityManager()
    }

    def cleanup() {
        CustomSecurityManager.unrestrictThread()
    }

    def 'getProperties'() {
        when:
        CustomSecurityManager.restrictThread()
        System.getProperties()

        then:
        thrown SecurityException

        when:
        CustomSecurityManager.unrestrictThread()
        System.getProperties()

        then:
        notThrown SecurityException
    }

    def 'write file'() {
        when:
        CustomSecurityManager.restrictThread()
        File.createTempFile 'aaa', 'txt'

        then:
        thrown SecurityException

        when:
        CustomSecurityManager.unrestrictThread()
        File.createTempFile 'aaa', 'txt'

        then:
        notThrown SecurityException
    }

    def 'get env var'() {
        when:
        CustomSecurityManager.restrictThread()
        System.getenv 'test'

        then:
        thrown SecurityException

        when:
        CustomSecurityManager.unrestrictThread()
        System.getenv 'test'

        then:
        notThrown SecurityException
    }
}

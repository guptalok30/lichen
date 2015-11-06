package com.peoplenet.lichen.mock

import spock.lang.Specification

class EventLedgerServiceSpec extends Specification {

    void 'gimme uuid'() {
        expect:
        println UUID.randomUUID().toString()
    }
}

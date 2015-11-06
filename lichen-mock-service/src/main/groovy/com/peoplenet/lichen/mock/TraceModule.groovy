package com.peoplenet.lichen.mock

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.AbstractModule
import com.google.inject.Singleton
import com.peoplenet.common.PeopleNetObjectMapperBuilder

class TraceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ObjectMapper).toInstance(new PeopleNetObjectMapperBuilder().build())
        bind(TraceHandler).in(Singleton)
        bind(EventLedgerService).in(Singleton)
    }

    static class Config {
        String eventLedgerUrl = 'event-ledger.service.consul'
    }
}

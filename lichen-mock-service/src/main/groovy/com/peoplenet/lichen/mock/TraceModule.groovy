package com.peoplenet.lichen.mock

import com.google.inject.AbstractModule
import com.google.inject.Singleton

class TraceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TraceHandler).in(Singleton)
        bind(EventLedgerService).in(Singleton)
    }

    static class Config {
        String eventLedgerUrl = 'event-ledger.service.consul'
    }
}

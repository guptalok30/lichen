package com.peoplenet.lichen.mock

import com.google.inject.Inject
import com.peoplenet.eventledger.client.EventLedgerApi
import com.peoplenet.eventledger.domain.EventId
import com.peoplenet.retrofit.client.ClientApiBuilder
import groovy.util.logging.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ratpack.exec.Blocking
import ratpack.exec.Operation
import ratpack.server.Service
import ratpack.server.StartEvent
import retrofit.mime.TypedString

@Slf4j
class EventLedgerService implements Service {
    Logger auditLog = LoggerFactory.getLogger('audit')
    EventLedgerApi eventLedgerApi
    TraceModule.Config config

    @Inject
    EventLedgerService(TraceModule.Config config) {
        this.config = config
    }

    @Override
    void onStart(StartEvent event) throws Exception {
        eventLedgerApi = new ClientApiBuilder().withEndpoint(config.eventLedgerUrl).build(EventLedgerApi)
    }

    Operation publishEvent(String event, String serviceName, String payload, Trace trace) {
        return Blocking.op {
            EventId eventId = eventLedgerApi.writeTextEvent(
                        'LICHEN', 'HACKATHON', UUID.randomUUID().toString(), event, new TypedString(payload))
            log.info("Published event ${eventId}")
        }.next(auditEvent(serviceName, event, trace))
    }

    Operation auditEvent(String serviceName, String event, Trace trace) {
        Operation.of {
            String kv = [
                    "duration=${trace.durationToNow()}",
                    "timestamp=${System.currentTimeMillis()}",
                    "service=${serviceName}",
                    "eventType=${event}",
                    "traceTag=${trace.traceId}",
                    "parentService=${trace.parentService}"
            ].join(", ")

            auditLog.info(kv)
        }
    }
}


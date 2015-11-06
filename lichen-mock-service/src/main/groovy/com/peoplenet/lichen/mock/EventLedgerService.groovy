package com.peoplenet.lichen.mock

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import com.google.common.net.MediaType
import com.google.inject.Inject
import com.peoplenet.eventledger.client.EventLedgerApi
import com.peoplenet.eventledger.domain.EventId
import com.peoplenet.retrofit.client.ClientApiBuilder
import groovy.util.logging.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ratpack.exec.Operation
import ratpack.exec.Promise
import ratpack.server.Service
import ratpack.server.StartEvent
import retrofit.mime.TypedByteArray

@Slf4j
class EventLedgerService implements Service {
    Logger auditLog = LoggerFactory.getLogger('audit')
    EventLedgerApi eventLedgerApi

    TraceModule.Config config
    ObjectMapper objectMapper

    @Inject
    EventLedgerService(TraceModule.Config config, ObjectMapper objectMapper) {
        this.config = config
        this.objectMapper = objectMapper
    }

    @Override
    void onStart(StartEvent event) throws Exception {
        eventLedgerApi = new ClientApiBuilder().withEndpoint(config.eventLedgerUrl).build(EventLedgerApi)
    }

    Operation publishEvent(String consumedEvent, String event, String serviceName, String payload, Trace trace) {
        Promise.ofLazy {
            decoratePayload(event, serviceName, trace, payload.bytes)
        }.blockingOp { byte[] payloadBytes ->

            EventId eventId = eventLedgerApi.writeBinaryEvent(
                    'LICHEN',
                    'HACKATHON',
                    UUID.randomUUID().toString(),
                    event,
                    new TypedByteArray(MediaType.OCTET_STREAM.toString(), payloadBytes))

            log.info("Published event ${eventId}")

        }.operation { auditEvent(serviceName, consumedEvent, trace) }
    }

    void auditEvent(String serviceName, String consumedEvent, Trace trace) {
        TraceAudit traceAudit = new TraceAudit(
                duration: trace.durationToNow(),
                timestamp: System.currentTimeMillis(),
                service: serviceName,
                consumedEvent: consumedEvent,
                traceId: trace.traceId,
                parentConsumedEvent: trace.parentConsumedEvent,
                parentService: trace.parentService
        )

        auditLog.info(objectMapper.writeValueAsString(traceAudit))
    }

    byte[] decoratePayload(String consumedEvent, String service, Trace trace, byte[] payload) {

        ByteArrayDataOutput out = ByteStreams.newDataOutput()
        out.with {
            writeByte(trace.trace ? 1 : 0)

            UUID traceId = UUID.fromString(trace.traceId)
            writeLong(traceId.mostSignificantBits)
            writeLong(traceId.leastSignificantBits)

            writeInt(payload.length)
            write(payload)
        }

        return out.toByteArray()
    }
}


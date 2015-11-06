package com.peoplenet.event.emitter.eventledger

import com.peoplenet.eventledger.client.EventLedgerApi
import groovy.transform.CompileStatic
import retrofit.mime.TypedString

import javax.inject.Inject
import javax.inject.Named
import java.security.MessageDigest

/**
 * Created by agupta on 11/5/15.
 */
@CompileStatic
@Named
class EventLedgerClient {

    private EventLedgerApi eventLedgerApi

    @Inject
    EventLedgerClient(EventLedgerApi eventLedgerApi) {
        this.eventLedgerApi = eventLedgerApi
    }


    void send(String data, String emitterType, String emitterId, String eventType) {
        eventLedgerApi.writeTextEvent(
                emitterType,
                emitterId,
                generateEventId(emitterId, eventType),
                eventType.toString(),
                new TypedString(data))
    }

    private String generateEventId(String emitterId, String eventType) {
        MessageDigest digest = MessageDigest.getInstance("MD5")
        String eventId = eventType + emitterId + new Date().time
        byte[] md5 = digest.digest(eventId.getBytes("UTF-8"))
        BigInteger bigInt = new BigInteger(1, md5)
        String hashtext
        for(hashtext = bigInt.toString(16); hashtext.length() < 32; hashtext = "0" + hashtext) {

        }
       return hashtext
    }
}

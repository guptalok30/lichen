package com.peoplenet.event.emitter.web

import com.peoplenet.event.emitter.eventledger.EventLedgerClient
import groovy.transform.CompileStatic
import org.springframework.web.bind.annotation.*

import javax.inject.Inject

/**
 * Created by agupta on 11/5/15.
 */
@CompileStatic
@RestController
@RequestMapping('/emitter')
class EventEmitterController {

    EventLedgerClient eventLedgerClient

    @Inject
    EventEmitterController(EventLedgerClient eventLedgerClient) {
        this.eventLedgerClient = eventLedgerClient
    }

    @RequestMapping(value = "/{eventType}", method = RequestMethod.GET)
    String eventEmitter(@PathVariable String eventType, @RequestParam Integer eventCount) {
        String eventData = 'This is a test event for hackathon' + UUID.randomUUID().toString()

        (1..eventCount).each {
            eventLedgerClient.send(eventData, 'HACKEMIT', "HACKID", eventType)
        }

        return 'Events dispatched'
    }
}

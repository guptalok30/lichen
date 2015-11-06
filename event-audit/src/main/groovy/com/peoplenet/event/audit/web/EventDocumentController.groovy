package com.peoplenet.event.audit.web

import com.peoplenet.event.audit.service.EventDocumentService
import groovy.transform.CompileStatic
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

import javax.inject.Inject
import javax.validation.Valid

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

/**
 * Created by agupta on 11/5/15.
 */
@CompileStatic
@RestController
@RequestMapping('/auditEvents')
class EventDocumentController {

    static final String INDEX = "lichen"
    static final String TYPE = "event_audit"
    EventDocumentService eventService

    @Inject
    EventDocumentController(EventDocumentService eventService) {
        this.eventService = eventService
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    String save(@Valid @RequestBody Map documentSource) {
        String responseString = eventService.save(INDEX, TYPE, documentSource)
        return responseString
    }

}

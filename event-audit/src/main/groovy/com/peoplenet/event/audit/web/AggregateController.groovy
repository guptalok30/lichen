package com.peoplenet.event.audit.web

import com.peoplenet.event.audit.domain.EventTraceAggregate
import com.peoplenet.event.audit.service.AggregateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by dhreines on 11/5/15.
 */
@RestController
class AggregateController {

    @Autowired
    AggregateService aggregateService


    @RequestMapping(value = "/eventtrace/aggregate", method = RequestMethod.GET)
    List<EventTraceAggregate> getEventTraceAggregates() {
        aggregateService.getAggregateTrace()
    }

}

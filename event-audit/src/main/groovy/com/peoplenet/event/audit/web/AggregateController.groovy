package com.peoplenet.event.audit.web

import com.peoplenet.event.audit.domain.EventServiceNodeAggregate
import com.peoplenet.event.audit.domain.EventTraceAggregate
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by dhreines on 11/5/15.
 */
@RestController
class AggregateController {


    @RequestMapping(value = "/eventtrace/aggregate", method = RequestMethod.GET)
    EventTraceAggregate getEventTraceAggregates() {

        EventTraceAggregate aggregate = new EventTraceAggregate()
        EventServiceNodeAggregate node = new EventServiceNodeAggregate()
        node.serviceName = 'abc'
        node.eventType = 'FAULT'
        node.count = 10
        node.meanDuration = 5
        aggregate.origin = node

        EventServiceNodeAggregate node1 = new EventServiceNodeAggregate()
        node1.serviceName = 'def'
        node1.eventType = 'FAULT_DEC'
        node1.count = 15
        node1.meanDuration = 5
        node.children.add(node1)

        EventServiceNodeAggregate node2 = new EventServiceNodeAggregate()
        node2.serviceName = 'xyz'
        node2.eventType = 'FAULT_GUIDANCE'
        node2.count = 20
        node2.meanDuration = 5
        node1.children.add(node2)

        EventServiceNodeAggregate node3 = new EventServiceNodeAggregate()
        node3.serviceName = 'ghi'
        node3.eventType = 'FAULT_DEC'
        node3.count = 20
        node3.meanDuration = 5
        node.children.add(node3)

        aggregate
    }

}

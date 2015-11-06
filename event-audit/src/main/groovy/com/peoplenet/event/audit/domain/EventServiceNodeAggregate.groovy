package com.peoplenet.event.audit.domain

import groovy.transform.Canonical
import groovy.transform.CompileStatic

/**
 * Created by dhreines on 11/5/15.
 */
@Canonical
@CompileStatic
class EventServiceNodeAggregate {

    String serviceName
    String eventType
    String parentServiceName
    String parentEventType
    Double avgDuration
    Integer count

    List<EventServiceNodeAggregate> children = []

}

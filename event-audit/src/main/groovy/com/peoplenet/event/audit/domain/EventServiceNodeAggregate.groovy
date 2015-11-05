package com.peoplenet.event.audit.domain

/**
 * Created by dhreines on 11/5/15.
 */
class EventServiceNodeAggregate {

    String serviceName
    String eventType
    Double meanDuration
    Integer count

    List<EventServiceNodeAggregate> children = []

}

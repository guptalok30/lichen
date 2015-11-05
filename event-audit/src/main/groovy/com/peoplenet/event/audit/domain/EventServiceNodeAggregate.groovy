package com.peoplenet.event.audit.domain

/**
 * Created by dhreines on 11/5/15.
 */
class EventServiceNodeAggregate {

    String name
    Double meanDuration
    Integer count

    EventServiceNodeAggregate[] children

}

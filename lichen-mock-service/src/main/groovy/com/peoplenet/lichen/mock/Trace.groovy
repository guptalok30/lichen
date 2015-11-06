package com.peoplenet.lichen.mock

class Trace {
    Long startTimeNanos
    String traceId
    String parentService
    String parentConsumedEvent
    Boolean trace

    Long durationToNow() {
        (System.nanoTime() - startTimeNanos) / 1_000_000
    }
}

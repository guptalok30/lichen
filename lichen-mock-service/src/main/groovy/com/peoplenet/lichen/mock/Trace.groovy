package com.peoplenet.lichen.mock

class Trace {
    Long startTimeNanos
    String traceId
    String parentService
    String parentEvent

    Long durationToNow() {
        (System.nanoTime() - startTimeNanos) / 1_000_000
    }
}

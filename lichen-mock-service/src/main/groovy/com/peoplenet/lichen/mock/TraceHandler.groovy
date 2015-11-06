package com.peoplenet.lichen.mock

import ratpack.groovy.handling.GroovyContext
import ratpack.groovy.handling.GroovyHandler

class TraceHandler extends GroovyHandler {

    @Override
    protected void handle(GroovyContext ctx) {
        String trace = ctx.request.headers.get('TRACE')
        if(trace == null) {
           ctx.next(ctx.single(new Trace(
                   trace: true,
                   startTimeNanos: System.nanoTime(),
                   traceId: UUID.randomUUID().toString())))
        } else if(trace == '1') {
           ctx.next(ctx.single(new Trace(
                    trace: true,
                    startTimeNanos: System.nanoTime(),
                    traceId: ctx.request.getHeaders().get('TRACE_ID'),
                    parentConsumedEvent: ctx.request.getHeaders().get('PARENT_CONSUMED_EVENT'),
                    parentService: ctx.request.getHeaders().get('PARENT_SERVICE'))))
        }

    }
}

import com.google.common.io.Resources
import com.peoplenet.common.ratpack.VersionHandler
import com.peoplenet.lichen.mock.EventLedgerService
import com.peoplenet.lichen.mock.TraceModule
import com.peoplenet.lichen.mock.TraceHandler
import com.peoplenet.lichen.mock.Trace
import ratpack.exec.Blocking
import ratpack.handling.Context

import static ratpack.groovy.Groovy.ratpack

ratpack {
    serverConfig {
        yaml(Resources.asByteSource(Resources.getResource('application.yml')))
        sysProps()
        env()

        require '/trace', TraceModule.Config
    }

    bindings {
        bindInstance new Random()
        bind VersionHandler
        module TraceModule
    }

    handlers {
        all(TraceHandler)

        //fault -> fault_decorator -> decorated fault
        post("lichen_fault_dec") { ctx ->
            renderIt(ctx, 'lichen_fault', 'lichen_dec_fault', 'fault_decorator', 100)
        }

        //decorated fault -> entity -> vehicle_update
        post("lichen_dec_fault_entity") { ctx ->
            renderIt(ctx, 'lichen_dec_fault', 'lichen_vehicle_update', 'entity', 100)
        }

        //decorated fault -> guidance service -> guidance
        post("lichen_dec_fault_guidance") { ctx ->
            renderIt(ctx, 'lichen_dec_fault', 'lichen_guidance', 'guidance-service', 1000)
        }

        //guidance -> entity -> nothing
        post("lichen_guidance_entity") { ctx ->
            renderIt(ctx, 'lichen_guidance', 'entity', 250)
        }

        //guidance -> notification -> nothing
        post("lichen_guidance_notification") { ctx ->
            renderIt(ctx, 'lichen_guidance', 'notification', 500)
        }

        get("health") {
            render "OK"
        }
        get("version", VersionHandler)
    }
}

void renderIt(Context ctx, String consumedEvent, String event, String service, int maxTimeout) {
    Blocking.op {
        sleep(ctx.get(Random).nextInt(maxTimeout) + 100)
    }.next(
        ctx.get(EventLedgerService).publishEvent(consumedEvent, event, service, """{"event":"${event}", "service":"${service}"}""", ctx.get(Trace))
    ).then {
        ctx.render "OK"
    }
}

void renderIt(Context ctx, String consumedEvent, String service, int maxTimeout) {
    Blocking.op {
        sleep(ctx.get(Random).nextInt(maxTimeout) + 100)
    }.next {
            ctx.get(EventLedgerService).auditEvent(service, consumedEvent, ctx.get(Trace))
    }.then {
        ctx.render "OK"
    }
}
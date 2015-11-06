package com.peoplenet.event.emitter

import com.peoplenet.eventledger.client.EventLedgerApi
import com.peoplenet.retrofit.client.ClientApiBuilder
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * Created by dhreines on 11/5/15.
 */
@ComponentScan(value = "com.peoplenet.event.emitter")
@Configuration
@EnableAutoConfiguration
class Application {

    static void main(final String[] args) {
        SpringApplication app = new SpringApplication(Application)
        app.run(args)
    }

    @Bean
    EventLedgerApi eventLedgerApi() {
        return new ClientApiBuilder<EventLedgerApi>()
                .withEndpoint('http://m2m-event-ledger-dev.connectedfleet.io:8080')
                .build(EventLedgerApi) as EventLedgerApi
    }

}

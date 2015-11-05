package com.peoplenet.event.audit

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * Created by dhreines on 11/5/15.
 */
@ComponentScan(value = "com.peoplenet.event.audit")
@Configuration
@EnableAutoConfiguration
class Application {

    static void main(final String[] args) {
        SpringApplication app = new SpringApplication(Application)
        app.run(args)
    }

}

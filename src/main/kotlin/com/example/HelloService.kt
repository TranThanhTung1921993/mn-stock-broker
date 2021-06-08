package com.example

import io.micronaut.context.annotation.Property
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class HelloService {

    private val LOG = LoggerFactory.getLogger(HelloService::class.java)

    @field:Property(name = "hello.service.greeting", defaultValue = "default value")
    lateinit var greeting: String

    fun sayHello() = greeting

    @EventListener
    internal fun onStartupEvent(event: StartupEvent) {
        LOG.debug("Startup ${HelloService::class.java.simpleName}")
    }
}
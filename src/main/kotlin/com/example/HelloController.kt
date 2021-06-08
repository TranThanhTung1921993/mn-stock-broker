package com.example

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import javax.inject.Inject

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("\${hello.controller.part}")
class HelloController {

    @Inject
    lateinit var config: GreetingConfig

    @Inject
    lateinit var service: HelloService

    @Get(value = "/", consumes = [MediaType.TEXT_PLAIN])
    fun hello(): String {
        return service.sayHello()
    }

    @Get(value = "/de", consumes = [MediaType.TEXT_PLAIN])
    fun greetInGerman(): String {
        return config.de
    }

    @Get(value = "/en", consumes = [MediaType.TEXT_PLAIN])
    fun greetInEnglish(): String {
        return config.en
    }

    @Get("/json")
    fun json(): Greeting {
        return Greeting()
    }
}
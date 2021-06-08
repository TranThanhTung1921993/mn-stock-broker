package com.example

import com.fasterxml.jackson.databind.node.ObjectNode
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class MnStockBrokerTest {

    @Inject
    @field:Client("/")
    lateinit var client: RxHttpClient

    @Inject
    lateinit var application: EmbeddedApplication<*>

    @Test
    fun testItWorks() {
        Assertions.assertTrue(application.isRunning)
    }

    @Test
    fun testHello() {
        val request: HttpRequest<Any> = HttpRequest.GET("/hello")
        val body = client.toBlocking().retrieve(request)
        Assertions.assertNotNull(body)
        Assertions.assertEquals(body, "Hello from service")
    }

    @Test
    fun returnsGermanGreeting() {
        val request: HttpRequest<Any> = HttpRequest.GET("/hello/de")
        val body = client.toBlocking().retrieve(request)
        Assertions.assertEquals(body, "Hallo")
    }

    @Test
    fun returnsEnglishGreeting() {
        val request: HttpRequest<Any> = HttpRequest.GET("/hello/en")
        val body = client.toBlocking().retrieve(request)
        Assertions.assertEquals(body, "Hello")
    }

    @Test
    fun returnsGreetingAsJson() {
        val result = client.toBlocking().retrieve("/hello/json", ObjectNode::class.java)
        Assertions.assertEquals("someJson", result.toString())
    }
}

package com.example.broker

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class MarketsControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: RxHttpClient

    @Test
    fun returnsListOfMarkets() {
        val request: HttpRequest<Any> = HttpRequest.GET("/markets")
        val list = client.toBlocking().retrieve(request, List::class.java)
        Assertions.assertEquals(7, list.size)
//        val markets: List<LinkedHashMap<String, String>> = list.filterIsInstance<LinkedHashMap<String, String>>()
//        assertThat(markets).extracting
    }
}
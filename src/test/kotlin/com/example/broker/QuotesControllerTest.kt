package com.example.broker

import com.example.broker.error.CustomError
import com.example.model.Quote
import com.example.model.Symbol
import com.example.store.InMemoryStore
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.net.URI
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import javax.inject.Inject

@MicronautTest
class QuotesControllerTest {

    private val logger = LoggerFactory.getLogger(QuotesControllerTest::class.java)

    @Inject
    @field:Client("/")
    lateinit var client: RxHttpClient

    @Inject
    lateinit var store: InMemoryStore

    @Test
    fun returnsQuotePerSymbol() {
        val apple = initRandomQuote("APPL")
        store.update(apple)

        val amazon = initRandomQuote("AMZN")
        store.update(amazon)

        val appleResult = client.toBlocking().retrieve("/quotes/APPL", Quote::class.java)
        logger.debug("APPLE Result: $appleResult")
        assertThat(apple).isEqualToComparingFieldByField(appleResult)

        val amazonResult = client.toBlocking().retrieve("/quotes/AMZN", Quote::class.java)
        logger.debug("AMAZON Result: $amazonResult")
        assertThat(amazon).isEqualToComparingFieldByField(amazonResult)
    }

    @Test
    fun returnsNotFoundOnUnsupportedSymbol() {
        try {
            val request: HttpRequest<Any> = HttpRequest.GET("/quotes/unsupported")
            client.toBlocking().retrieve(request)
        } catch (e: HttpClientResponseException) {
            Assertions.assertEquals(HttpStatus.NOT_FOUND, e.response.status)
            logger.debug("Body: ${e.response.body}")
            val notFoundError = e.response.getBody(CustomError::class.java)
            Assertions.assertTrue(notFoundError.isPresent)
            Assertions.assertEquals(HttpStatus.NOT_FOUND.code, notFoundError.get().status)
            Assertions.assertEquals(HttpStatus.NOT_FOUND.name, notFoundError.get().error)
            Assertions.assertEquals("Quote for symbol not available", notFoundError.get().message)
            Assertions.assertEquals("/quotes/unsupported", notFoundError.get().path)
        }
    }

    private fun randomValue(): BigDecimal {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1.0, 100.0))
    }

    private fun initRandomQuote(symbolValue: String): Quote {
        return Quote(
            symbol = Symbol(symbolValue),
            bid = randomValue(),
            ask = randomValue(),
            lastPrice = randomValue(),
            volume = randomValue()
        )
    }
}
package com.example.broker

import com.example.broker.error.CustomError
import com.example.model.Quote
import com.example.store.InMemoryStore
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import java.util.*
import javax.inject.Inject

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/quotes")
class QuotesController {

    @Inject
    lateinit var store: InMemoryStore

    @Get("/{symbol}")
    fun getQuote(@PathVariable symbol: String): HttpResponse<Any> {
        val quote: Optional<Quote> = store.fetchQuote(symbol)
        if (quote.isEmpty()) {
            val notFoundError = CustomError(
                status = HttpStatus.NOT_FOUND.code,
                error = HttpStatus.NOT_FOUND.name,
                message = "Quote for symbol not available",
                path = "/quotes/$symbol"
            )
            return HttpResponse.notFound(notFoundError)
        }
        return HttpResponse.ok(quote.get())
    }
}
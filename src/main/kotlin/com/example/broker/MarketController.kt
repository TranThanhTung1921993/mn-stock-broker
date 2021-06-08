package com.example.broker

import com.example.model.Symbol
import com.example.store.InMemoryStore
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import javax.inject.Inject

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/markets")
class MarketController {

    @Inject
    lateinit var store: InMemoryStore

    @Get(value = "/")
    fun all(): List<Symbol> {
        return store.getAllSymbols()
    }
}
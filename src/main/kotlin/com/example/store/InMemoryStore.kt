package com.example.store

import com.example.model.Quote
import com.example.model.Symbol
import io.netty.util.internal.ThreadLocalRandom
import java.math.BigDecimal
import java.util.*
import javax.inject.Singleton
import kotlin.collections.HashMap

@Singleton
class InMemoryStore {

    private val symbols: List<Symbol> = listOf("APPL", "AMZN", "FB", "GOOG", "MSFT", "NFLX", "TSLA").map { Symbol(it) }

    private val current: ThreadLocalRandom = ThreadLocalRandom.current()

    private val cachedQuotes: HashMap<String, Quote> = HashMap()

    init {
        symbols.forEach { symbol -> cachedQuotes[symbol.value] = randomQuote(symbol) }
    }

    fun getAllSymbols(): List<Symbol> {
        return symbols
    }

    fun fetchQuote(symbol: String): Optional<Quote> {
        return Optional.ofNullable(cachedQuotes[symbol])
    }

    fun update(quote: Quote) {
        cachedQuotes[quote.symbol.value] = quote
    }

    private fun randomQuote(symbol: Symbol): Quote {
        return Quote(
            symbol = symbol,
            bid = randomValue(),
            ask = randomValue(),
            lastPrice = randomValue(),
            volume = randomValue()
        )
    }

    private fun randomValue(): BigDecimal {
        return BigDecimal.valueOf(current.nextDouble(1.0, 100.0))
    }
}
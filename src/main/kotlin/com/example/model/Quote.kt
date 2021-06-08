package com.example.model

import java.math.BigDecimal

data class Quote(
    val symbol: Symbol,
    val bid: BigDecimal,
    val ask: BigDecimal,
    val lastPrice: BigDecimal,
    val volume: BigDecimal
) {}

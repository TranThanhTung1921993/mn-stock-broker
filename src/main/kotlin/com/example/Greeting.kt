package com.example

import java.math.BigDecimal
import java.time.Instant

data class Greeting(
    val myText: String = "Hello World",
    val id: BigDecimal = BigDecimal.ONE,
    val timeUTC: Instant = Instant.now()
)

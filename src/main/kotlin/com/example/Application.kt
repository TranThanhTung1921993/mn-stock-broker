package com.example

import io.micronaut.runtime.Micronaut.build

fun main(args: Array<String>) {
    build()
        .args(*args)
        .propertySources(HelloDevConfig.default())
        .banner(false)
        .packages("com.example")
        .start()
}


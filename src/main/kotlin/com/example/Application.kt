package com.example

import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import io.micronaut.runtime.Micronaut.*

fun main(args: Array<String>) {
    build()
        .args(*args)
        .propertySources(HelloDevConfig.default(), HelloProdConfig.default())
//        .environments(Environment.TEST)
//        .environments("prod")
//        .deduceEnvironment(false)
        .banner(false)
        .packages("com.example")
        .start()
}


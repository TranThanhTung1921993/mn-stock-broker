package com.example

import io.micronaut.context.annotation.ConfigurationInject
import io.micronaut.context.annotation.ConfigurationProperties


@ConfigurationProperties(value = "hello.language")
data class HelloDevConfig @ConfigurationInject constructor(
//    val vietnamese: String,
    val chinese: String
) {

    companion object {
        fun default(): io.micronaut.context.env.PropertySource {
            val defaults: Map<String, Any> = mapOf(
//                "hello.language.vietnamese" to "Xin chao dev",
                "hello.language.chinese" to "Ni hao dev",
            )
            return io.micronaut.context.env.PropertySource.of("property-hello-language", defaults, -10000)
        }
    }
}
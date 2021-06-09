package com.example

import io.micronaut.context.annotation.ConfigurationInject
import io.micronaut.context.annotation.ConfigurationProperties
import javax.validation.constraints.NotBlank

@ConfigurationProperties("hello.config.greeting")
data class GreetingConfig @ConfigurationInject constructor(
    @NotBlank val en: String,
    @NotBlank val de: String
)
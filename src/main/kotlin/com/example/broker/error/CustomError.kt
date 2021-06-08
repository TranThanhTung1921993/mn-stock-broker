package com.example.broker.error

data class CustomError(val status: Int, val error: String, val message: String, val path: String)

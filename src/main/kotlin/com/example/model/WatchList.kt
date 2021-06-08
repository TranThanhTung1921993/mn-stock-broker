package com.example.model

data class WatchList(val symbols: List<Symbol>) {
    constructor() : this(listOf<Symbol>())
}

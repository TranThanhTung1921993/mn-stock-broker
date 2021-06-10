package com.example.store

import com.example.broker.account.WatchListController
import com.example.model.Symbol
import com.example.model.WatchList
import java.util.*
import javax.inject.Singleton
import kotlin.collections.HashMap

@Singleton
class InMemoryAccountStore {

    private val watchListsPerAccount: HashMap<UUID, WatchList> = HashMap()

    init {
        val symbols = listOf("APPL", "AMZN", "NFLX").map { s -> Symbol(s) }
        val watchList = WatchList(symbols)
        updateWatchList(WatchListController.ACCOUNT_ID, watchList)
    }

    fun getWatchList(accountId: UUID?): WatchList {
        return watchListsPerAccount.getOrDefault(accountId, WatchList())
    }

    fun updateWatchList(accountId: UUID?, watchList: WatchList): WatchList {
        accountId?.let { watchListsPerAccount.put(it, watchList) }
        return getWatchList(accountId)
    }

    fun deleteWatchList(accountId: UUID) {
        watchListsPerAccount.remove(accountId)
    }
}
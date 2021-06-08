package com.example.store

import com.example.model.WatchList
import java.util.*
import javax.inject.Singleton
import kotlin.collections.HashMap

@Singleton
class InMemoryAccountStore {

    val watchListsPerAccount: HashMap<UUID, WatchList> = HashMap()

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
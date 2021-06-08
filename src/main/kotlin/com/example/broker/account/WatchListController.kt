package com.example.broker.account

import com.example.model.WatchList
import com.example.store.InMemoryAccountStore
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import java.util.*
import javax.inject.Inject

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/account/watchlist")
class WatchListController {

    @Inject
    lateinit var store: InMemoryAccountStore

    @Get(produces = [MediaType.APPLICATION_JSON])
    fun get(): WatchList {
        return store.getWatchList(ACCOUNT_ID)
    }

    @Put(
        consumes = [MediaType.APPLICATION_JSON],
        produces = [MediaType.APPLICATION_JSON]
    )
    fun update(@Body watchList: WatchList): WatchList {
        return store.updateWatchList(ACCOUNT_ID, watchList)
    }

    @Delete(
        value = "/{accountId}",
        consumes = [MediaType.APPLICATION_JSON],
        produces = [MediaType.APPLICATION_JSON]
    )
    fun delete(@PathVariable accountId: UUID) {
        store.deleteWatchList(accountId)
    }

    companion object {
        val ACCOUNT_ID: UUID = UUID.randomUUID()
    }
}
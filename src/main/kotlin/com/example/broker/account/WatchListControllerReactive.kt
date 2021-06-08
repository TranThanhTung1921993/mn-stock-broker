package com.example.broker.account

import com.example.model.WatchList
import com.example.store.InMemoryAccountStore
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.reactivex.Single
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/account/watchlist-reactive")
class WatchListControllerReactive {

    @Inject
    lateinit var store: InMemoryAccountStore

    @Get(produces = [MediaType.APPLICATION_JSON])
    @ExecuteOn(TaskExecutors.IO)
    fun get(): WatchList {
        LOGGER.debug("getWatchList - ${Thread.currentThread().name}")
        return store.getWatchList(ACCOUNT_ID)
    }

    @Get(
        value = "/single",
        produces = [MediaType.APPLICATION_JSON]
    )
    fun getAsSingle(): Single<WatchList> {
        return Single.fromCallable {
            LOGGER.debug("getAsSingle - ${Thread.currentThread().name}")
            return@fromCallable store.getWatchList(ACCOUNT_ID)
        }
    }

    companion object {
        val ACCOUNT_ID: UUID = UUID.randomUUID()
        private val LOGGER = LoggerFactory.getLogger(WatchListControllerReactive::class.java)
    }
}
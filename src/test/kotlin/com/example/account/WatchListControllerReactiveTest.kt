package com.example.account

import com.example.broker.account.WatchListController
import com.example.broker.account.WatchListControllerReactive
import com.example.model.WatchList
import com.example.store.InMemoryAccountStore
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.reactivex.Single
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.platform.commons.logging.LoggerFactory
import javax.inject.Inject
import javax.validation.constraints.AssertTrue

@MicronautTest
class WatchListControllerReactiveTest {

    @Inject
    @field:Client("/account/watchlist-reactive")
    lateinit var client: RxHttpClient

    @Inject
    lateinit var store: InMemoryAccountStore

    @Test
    fun returnsEmptyWatchListForAccount() {
        val result: Single<WatchList> =
            client.retrieve(HttpRequest.GET<Any>("/"), WatchList::class.java).singleOrError()
        Assertions.assertTrue(result.blockingGet().symbols.isEmpty())
        Assertions.assertTrue(store.getWatchList(TEST_ACCOUNT_ID).symbols.isEmpty())
    }

    companion object {
        val TEST_ACCOUNT_ID = WatchListControllerReactive.ACCOUNT_ID
        val LOGGER = LoggerFactory.getLogger(WatchListControllerReactiveTest::class.java)
    }
}
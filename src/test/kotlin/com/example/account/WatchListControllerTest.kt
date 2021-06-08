package com.example.account

import com.example.broker.account.WatchListController
import com.example.model.Symbol
import com.example.model.WatchList
import com.example.store.InMemoryAccountStore
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject

@MicronautTest
class WatchListControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: RxHttpClient

    @Inject
    lateinit var store: InMemoryAccountStore

    @Test
    fun unauthorizedAccessIsForbidden() {
        try {
            client.toBlocking().retrieve(ACCOUNT_WATCHLIST)
            fail("Should fail if no exception is thrown")
        } catch (e: HttpClientResponseException) {
            Assertions.assertEquals(HttpStatus.UNAUTHORIZED, e.status)
        }
    }

    @Test
    fun returnsEmptyWatchListForAccount() {
        val token = givenMyUserIsLoggedIn()
        val request = HttpRequest.GET<Any>(ACCOUNT_WATCHLIST)
            .accept(MediaType.APPLICATION_JSON)
            .bearerAuth(token?.accessToken)
        val result = client.toBlocking().retrieve(request, WatchList::class.java)
        Assertions.assertTrue(result.symbols.isEmpty())
        Assertions.assertTrue(store.getWatchList(TEST_ACCOUNT_ID).symbols.isEmpty())
    }

    @Test
    fun returnsWatchListForAccount() {
        val token = givenMyUserIsLoggedIn()
        val symbols = listOf("APPL", "AMZN", "NFLX").map { s -> Symbol(s) }
        val watchList = WatchList(symbols)
        store.updateWatchList(TEST_ACCOUNT_ID, watchList)
        val request = HttpRequest.GET<Any>(ACCOUNT_WATCHLIST)
            .accept(MediaType.APPLICATION_JSON)
            .bearerAuth(token?.accessToken)
        val result = client.toBlocking().retrieve(request, WatchList::class.java)
        Assertions.assertEquals(3, result.symbols.size)
        Assertions.assertEquals(3, store.getWatchList(TEST_ACCOUNT_ID).symbols.size)
    }

    @Test
    fun canUpdateWatchListForAccount() {
        val token = givenMyUserIsLoggedIn()
        val symbols = listOf("APPL", "AMZN", "NFLX").map { s -> Symbol(s) }
        val watchList = WatchList(symbols)
        store.updateWatchList(TEST_ACCOUNT_ID, watchList)
        val request = HttpRequest.PUT(ACCOUNT_WATCHLIST, watchList)
            .accept(MediaType.APPLICATION_JSON)
            .bearerAuth(token?.accessToken)
        val added: HttpResponse<Any> = client.toBlocking().exchange(request)
        Assertions.assertEquals(HttpStatus.OK, added.status)
        Assertions.assertEquals(watchList, store.getWatchList(TEST_ACCOUNT_ID))
    }

    @Test
    fun canDeleteWatchListForAccount() {
        val token = givenMyUserIsLoggedIn()
        val symbols = listOf("APPL", "AMZN", "NFLX").map { s -> Symbol(s) }
        val watchList = WatchList(symbols)
        store.updateWatchList(TEST_ACCOUNT_ID, watchList)
        Assertions.assertFalse(store.getWatchList(TEST_ACCOUNT_ID).symbols.isEmpty())
        val request = HttpRequest.DELETE<Any>("$ACCOUNT_WATCHLIST/$TEST_ACCOUNT_ID")
            .accept(MediaType.APPLICATION_JSON)
            .bearerAuth(token?.accessToken)
        val deleted: HttpResponse<Any> = client.toBlocking().exchange(request)
        Assertions.assertEquals(HttpStatus.OK, deleted.status)
        Assertions.assertTrue(store.getWatchList(TEST_ACCOUNT_ID).symbols.isEmpty())
    }

    private fun givenMyUserIsLoggedIn(): BearerAccessRefreshToken? {
        val credentials = UsernamePasswordCredentials("my-user", "secret")
        val loginRequest = HttpRequest.POST("/login", credentials)
        val loginResponse = client.toBlocking().exchange(loginRequest, BearerAccessRefreshToken::class.java)
        val token = loginResponse.body()
        Assertions.assertNotNull(token)
        Assertions.assertEquals(HttpStatus.OK, loginResponse.status)
        Assertions.assertEquals("my-user", token?.username)
        LOGGER.debug("Login Bearer Token: ${token?.accessToken} expires in ${token?.expiresIn}")
        return token
    }

    companion object {
        val TEST_ACCOUNT_ID = WatchListController.ACCOUNT_ID
        val LOGGER: Logger = LoggerFactory.getLogger(WatchListControllerTest::class.java)
        val ACCOUNT_WATCHLIST = "/account/watchlist"
    }
}
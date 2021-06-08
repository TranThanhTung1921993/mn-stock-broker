package com.example.auth.jwt

import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class AuthenticationProviderUserPassword : AuthenticationProvider {

    companion object {
        val LOGGER = LoggerFactory.getLogger(AuthenticationProviderUserPassword::class.java)
    }

    override fun authenticate(
        httpRequest: HttpRequest<*>?,
        authenticationRequest: AuthenticationRequest<*, *>?
    ): Publisher<AuthenticationResponse> {
        return Flowable.create((FlowableOnSubscribe {
            val identity: Any? = authenticationRequest?.identity
            val secret: Any? = authenticationRequest?.secret
            LOGGER.debug("User $identity tries to login...")
            if (identity?.equals("my-user") == true && secret?.equals("secret") == true) {
                it.onNext(UserDetails(identity as String, arrayListOf()))
                it.onComplete()
                return@FlowableOnSubscribe
            }
            it.onError(AuthenticationException(AuthenticationFailed("Wrong username or password!")))
        }), BackpressureStrategy.ERROR)
    }
}
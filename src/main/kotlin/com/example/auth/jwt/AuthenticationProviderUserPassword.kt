package com.example.auth.jwt

import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.FlowableOnSubscribe
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class AuthenticationProviderUserPassword : AuthenticationProvider {

    override fun authenticate(
        httpRequest: HttpRequest<*>?,
        authenticationRequest: AuthenticationRequest<*, *>
    ): Publisher<AuthenticationResponse> {

        return Flowable.create({ emitter: FlowableEmitter<AuthenticationResponse> ->
            LOGGER.debug("User ${authenticationRequest.identity} tries to login...")
            if (authenticationRequest.identity == "my-user" && authenticationRequest.secret == "secret") {
                emitter.onNext(UserDetails(authenticationRequest.identity as String, ArrayList()))
                emitter.onComplete()
            } else {
                emitter.onError(AuthenticationException(AuthenticationFailed()))
            }
        }, BackpressureStrategy.ERROR)
    }

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(AuthenticationProviderUserPassword::class.java)
    }
}
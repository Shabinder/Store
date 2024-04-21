package org.mobilenativefoundation.sample.ev.xplat.foundation.networking.impl

import com.apollographql.apollo3.ApolloClient
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import org.mobilenativefoundation.sample.ev.xplat.foundation.networking.api.NetworkingClient
import org.mobilenativefoundation.sample.ev.xplat.foundation.networking.api.NetworkingComponent

@Component
abstract class RealNetworkingComponent : NetworkingComponent {

    @Provides
    fun provideApolloClient(): ApolloClient = ApolloClient.Builder()
        .serverUrl("https://api.chargetrip.io/graphql")
        .addHttpHeader(
            "x-client-id", Env.X_CLIENT_ID,
        )
        .addHttpHeader(
            "x-app-id", Env.X_APP_ID
        )
        .addHttpHeader(
            "x-app-identifier", Env.X_APP_IDENTIFIER
        )
        .addHttpHeader(
            "x-app-fingerprint", Env.X_APP_FINGERPRINT
        )
        .build()

    @Provides
    fun bindNetworkingClient(impl: RealNetworkingClient): NetworkingClient = impl
}
package org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.impl

import com.apollographql.apollo3.ApolloClient
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.NetworkingClient

@Inject
class RealNetworkingClient(
    private val apolloClient: ApolloClient
) : NetworkingClient {
}
package org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.impl

import com.apollographql.apollo3.ApolloClient
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.GetUserQuery
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.NetworkingClient

@Inject
class RealNetworkingClient(
    private val apolloClient: ApolloClient
) : NetworkingClient {
    override suspend fun getUser(query: GetUserQuery): GetUserQuery.Data? {
        val response = apolloClient.query(query).execute()
        return response.data
    }
}
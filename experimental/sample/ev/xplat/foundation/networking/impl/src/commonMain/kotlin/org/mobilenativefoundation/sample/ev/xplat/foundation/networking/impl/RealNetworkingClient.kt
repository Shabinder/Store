package org.mobilenativefoundation.sample.ev.xplat.foundation.networking.impl

import com.apollographql.apollo3.ApolloClient
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.ev.xplat.foundation.networking.api.GetNearbyStationsQuery
import org.mobilenativefoundation.sample.ev.xplat.foundation.networking.api.NetworkingClient

@Inject
class RealNetworkingClient(
    private val apolloClient: ApolloClient
) : NetworkingClient {
    override suspend fun getNearbyStations(query: GetNearbyStationsQuery): GetNearbyStationsQuery.Data? {
        val response = apolloClient.query(query).execute()
        return response.data
    }
}
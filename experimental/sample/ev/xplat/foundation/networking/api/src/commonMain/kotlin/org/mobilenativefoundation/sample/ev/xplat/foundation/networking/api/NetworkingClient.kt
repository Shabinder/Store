package org.mobilenativefoundation.sample.ev.xplat.foundation.networking.api

interface NetworkingClient {
    suspend fun getNearbyStations(
        query: GetNearbyStationsQuery
    ): GetNearbyStationsQuery.Data?
}
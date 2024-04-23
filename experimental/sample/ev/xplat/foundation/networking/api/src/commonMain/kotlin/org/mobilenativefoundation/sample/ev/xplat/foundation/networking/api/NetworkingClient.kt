package org.mobilenativefoundation.sample.ev.xplat.foundation.networking.api

interface NetworkingClient {
    suspend fun getNearbyChargingStations(
        query: GetNearbyChargingStationsQuery
    ): GetNearbyChargingStationsQuery.Data?
}
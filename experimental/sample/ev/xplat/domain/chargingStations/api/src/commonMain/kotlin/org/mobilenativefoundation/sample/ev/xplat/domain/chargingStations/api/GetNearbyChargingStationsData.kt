package org.mobilenativefoundation.sample.ev.xplat.domain.chargingStations.api

import org.mobilenativefoundation.sample.ev.xplat.foundation.networking.api.GetNearbyChargingStationsQuery

data class GetNearbyChargingStationsData(
    val nearbyChargingStations: List<GetNearbyChargingStationsQuery.StationAround>
)

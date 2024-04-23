package org.mobilenativefoundation.sample.ev.xplat.common.market

import org.mobilenativefoundation.market.Market
import org.mobilenativefoundation.sample.ev.xplat.foundation.networking.api.GetNearbyChargingStationsQuery

data class EvState(
    val nearbyChargingStations: List<GetNearbyChargingStationsQuery.StationAround>
) : Market.State
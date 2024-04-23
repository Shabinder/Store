package org.mobilenativefoundation.sample.ev.xplat.domain.chargingStations.api

import org.mobilenativefoundation.sample.ev.xplat.foundation.networking.api.GetNearbyChargingStationsQuery
import org.mobilenativefoundation.store.store5.Store

typealias NearbyChargingStationsStore = Store<GetNearbyChargingStationsQuery, GetNearbyChargingStationsData>

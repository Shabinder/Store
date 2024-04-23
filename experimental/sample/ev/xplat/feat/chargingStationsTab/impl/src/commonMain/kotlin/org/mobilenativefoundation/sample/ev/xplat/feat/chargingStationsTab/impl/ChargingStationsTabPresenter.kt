package org.mobilenativefoundation.sample.ev.xplat.feat.chargingStationsTab.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.ev.xplat.feat.chargingStationsTab.api.ChargingStation
import org.mobilenativefoundation.sample.ev.xplat.feat.chargingStationsTab.api.ChargingStationsTab

@Inject
class ChargingStationsTabPresenter(private val chargingStationsTabWarehouse: ChargingStationsTabWarehouse) :
    ChargingStationsTab.Presenter {

    @Composable
    override fun present(): ChargingStationsTab.State {
        val state = chargingStationsTabWarehouse.state.collectAsState()
        return state.value.nearbyChargingStations.map {
            ChargingStation(it.id, it.name.toString())
        }.let {
            ChargingStationsTab.State(it) { event ->
                when (event) {
                    is ChargingStationsTab.Event.TapChargingStation -> {}
                    ChargingStationsTab.Event.GetNearbyChargingStations -> {
                        chargingStationsTabWarehouse.dispatch(ChargingStationsTabAction.Async.GetNearbyChargingStations)
                    }
                }
            }
        }
    }
}
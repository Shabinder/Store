package org.mobilenativefoundation.sample.ev.xplat.feat.chargingStationsTab.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.ev.xplat.feat.chargingStationsTab.api.ChargingStationsTab

@Inject
class ChargingStationsTabUi : ChargingStationsTab.Ui {
    @Composable
    override fun Content(state: ChargingStationsTab.State, modifier: Modifier) {


        Column {
            Button(
                onClick = {
                    state.eventSink(ChargingStationsTab.Event.GetNearbyChargingStations)
                }
            ) {
                Text("Get nearby charging stations")
            }


            state.nearbyChargingStations.forEach {
                Column {
                    Text(it.id)
                    Text(it.name)
                }
            }
        }
    }
}
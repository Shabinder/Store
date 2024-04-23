package org.mobilenativefoundation.sample.ev.xplat.feat.chargingStationsTab.api

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.presenter.Presenter as CircuitPresenter
import com.slack.circuit.runtime.ui.Ui as CircuitUi

interface ChargingStationsTab : Screen {
    data class State(
        val nearbyChargingStations: List<ChargingStation>,
        val eventSink: (event: Event) -> Unit
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data class TapChargingStation(val chargingStationId: String) : Event
        data object GetNearbyChargingStations: Event
    }

    interface Ui : CircuitUi<State>
    interface Presenter : CircuitPresenter<State>
}
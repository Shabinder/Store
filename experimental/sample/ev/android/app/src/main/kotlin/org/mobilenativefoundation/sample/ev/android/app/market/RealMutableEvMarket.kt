package org.mobilenativefoundation.sample.ev.android.app.market

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.ev.xplat.common.market.EvState

@Inject
class RealMutableEvMarket : MutableEvMarket {
    private val _state = MutableStateFlow<EvState>(
        EvState(
            nearbyChargingStations = emptyList()
        )
    )

    override val state: StateFlow<EvState> = _state.asStateFlow()

    override fun updateState(nextState: EvState) {
        _state.value = nextState
    }
}
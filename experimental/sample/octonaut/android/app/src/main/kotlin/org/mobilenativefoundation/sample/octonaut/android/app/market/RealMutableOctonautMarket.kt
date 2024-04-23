package org.mobilenativefoundation.sample.octonaut.android.app.market

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.OctonautMarketState

@Inject
class RealMutableOctonautMarket : MutableOctonautMarket {
    private val _state = MutableStateFlow<OctonautMarketState>(
        OctonautMarketState(
            repositories = emptyList()
        )
    )

    override val state: StateFlow<OctonautMarketState> = _state.asStateFlow()

    override fun updateState(nextState: OctonautMarketState) {
        _state.value = nextState
    }
}
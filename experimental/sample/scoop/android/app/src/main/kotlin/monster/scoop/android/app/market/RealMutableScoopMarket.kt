package monster.scoop.android.app.market

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.tatarka.inject.annotations.Inject
import monster.scoop.xplat.common.market.ScoopState

@Inject
class RealMutableScoopMarket : MutableScoopMarket {
    private val _state = MutableStateFlow(ScoopState())

    override val state: StateFlow<ScoopState> = _state.asStateFlow()

    override fun updateState(nextState: ScoopState) {
        _state.value = nextState
    }
}
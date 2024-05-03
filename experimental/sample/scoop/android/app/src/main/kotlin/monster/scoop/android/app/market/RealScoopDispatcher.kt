package monster.scoop.android.app.market

import me.tatarka.inject.annotations.Inject
import monster.scoop.xplat.common.market.ScoopAction
import monster.scoop.xplat.common.market.ScoopDispatcher

@Inject
class RealScoopDispatcher(
    private val mutableMarket: MutableScoopMarket,
    private val rootReducer: RootReducer
) : ScoopDispatcher {
    override fun dispatch(action: ScoopAction) {
        val nextState = rootReducer.reduce(mutableMarket.state.value, action)
        mutableMarket.updateState(nextState)
    }
}






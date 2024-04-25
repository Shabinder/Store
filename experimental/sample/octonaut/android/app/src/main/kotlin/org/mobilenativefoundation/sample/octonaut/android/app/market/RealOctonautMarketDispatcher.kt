package org.mobilenativefoundation.sample.octonaut.android.app.market

import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.OctonautMarketAction
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.OctonautMarketDispatcher

@Inject
class RealOctonautMarketDispatcher(
    private val mutableMarket: MutableOctonautMarket,
    private val rootReducer: RootReducer
) : OctonautMarketDispatcher {
    override fun dispatch(action: OctonautMarketAction) {
        val nextState = rootReducer.reduce(mutableMarket.state.value, action)
        mutableMarket.updateState(nextState)
    }
}






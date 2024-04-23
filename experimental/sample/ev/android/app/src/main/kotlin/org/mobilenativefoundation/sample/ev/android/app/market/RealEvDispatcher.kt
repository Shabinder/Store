package org.mobilenativefoundation.sample.ev.android.app.market

import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.ev.xplat.common.market.EvAction
import org.mobilenativefoundation.sample.ev.xplat.common.market.EvDispatcher

@Inject
class RealEvDispatcher(
    private val mutableEvMarket: MutableEvMarket
) : EvDispatcher {
    override fun dispatch(action: EvAction) {
        mutableEvMarket.updateState(action.nextState)
    }
}
package org.mobilenativefoundation.sample.ev.xplat.common.market

import org.mobilenativefoundation.market.Market

data class EvAction(
    val nextState: EvState
): Market.Action
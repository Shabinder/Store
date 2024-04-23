package org.mobilenativefoundation.sample.octonaut.xplat.common.market

import org.mobilenativefoundation.market.Market

data class OctonautMarketAction(
    val nextState: OctonautMarketState
): Market.Action
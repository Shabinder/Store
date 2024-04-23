package org.mobilenativefoundation.sample.octonaut.xplat.common.market

import org.mobilenativefoundation.market.Market

data class OctonautMarketState(
    val repositories: List<Any>
) : Market.State
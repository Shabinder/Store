package org.mobilenativefoundation.sample.octonaut.xplat.common.market

import org.mobilenativefoundation.market.Market
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.User
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.GetUserQuery

sealed interface OctonautMarketAction: Market.Action {
    data class UpdateUser(
        val user: GetUserQuery.User?
    ): OctonautMarketAction
}
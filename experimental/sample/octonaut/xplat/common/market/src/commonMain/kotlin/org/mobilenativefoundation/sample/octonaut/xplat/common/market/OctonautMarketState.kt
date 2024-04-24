package org.mobilenativefoundation.sample.octonaut.xplat.common.market

import org.mobilenativefoundation.market.Market
import org.mobilenativefoundation.sample.octonaut.xplat.domain.notifications.api.Notification
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.User

data class OctonautMarketState(
    val repositories: List<Any> = emptyList(),
    val user: User? = null,
    val notifications: List<Notification> = emptyList()
) : Market.State
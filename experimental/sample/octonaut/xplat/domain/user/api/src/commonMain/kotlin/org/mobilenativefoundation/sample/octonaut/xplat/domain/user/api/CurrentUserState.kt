package org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api

import org.mobilenativefoundation.market.Market

data class CurrentUserState(
    val user: User? = null
): Market.State

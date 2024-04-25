package org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api

import org.mobilenativefoundation.market.MarketSupplier
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.GetUserQuery

typealias CurrentUserSupplier = MarketSupplier<GetUserQuery>
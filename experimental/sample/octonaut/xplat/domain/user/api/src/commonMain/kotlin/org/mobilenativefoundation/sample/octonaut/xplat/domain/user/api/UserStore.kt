package org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api

import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.GetUserQuery
import org.mobilenativefoundation.store.store5.Store

typealias UserStore = Store<GetUserQuery, GetUserQuery.User>
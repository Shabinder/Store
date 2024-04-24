package org.mobilenativefoundation.sample.octonaut.xplat.domain.notifications.api

import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.ListNotificationsQueryParams
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.ListNotificationsResponse
import org.mobilenativefoundation.store.store5.Store

typealias NotificationsStore = Store<ListNotificationsQueryParams, ListNotificationsResponse>


package org.mobilenativefoundation.sample.octonaut.android.app.market.reducers

import org.mobilenativefoundation.market.Market
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.OctonautMarketAction
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.OctonautMarketState
import org.mobilenativefoundation.sample.octonaut.xplat.domain.notifications.api.Notification
import org.mobilenativefoundation.sample.octonaut.xplat.domain.notifications.api.NotificationsState

val notificationsReducer: Market.Reducer<OctonautMarketState, OctonautMarketAction> =
    Market.Reducer { state, action ->
        if (action is OctonautMarketAction.UpdateNotifications) {
            val notifications = action.notifications.notifications.map {
                Notification(
                    it.id,
                    it.unread,
                    it.updatedAt,
                    it.lastReadAt,
                    it.url,
                    it.subscriptionUrl
                )
            }

            val byId = notifications.associateBy { it.id }
            val allIds = notifications.map { it.id }

            val notificationsState = NotificationsState(
                byId = byId,
                allIds = allIds
            )

            state.copy(notifications = notificationsState)
        } else {
            state
        }
    }
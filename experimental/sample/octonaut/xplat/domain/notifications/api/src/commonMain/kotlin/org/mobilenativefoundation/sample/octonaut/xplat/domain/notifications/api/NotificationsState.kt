package org.mobilenativefoundation.sample.octonaut.xplat.domain.notifications.api

import org.mobilenativefoundation.market.Market

data class NotificationsState(
    val byId: Map<String, Notification> = mapOf(),
    val allIds: List<String> = emptyList()
): Market.State

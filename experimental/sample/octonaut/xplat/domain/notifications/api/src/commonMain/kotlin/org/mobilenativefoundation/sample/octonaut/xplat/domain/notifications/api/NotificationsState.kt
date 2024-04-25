package org.mobilenativefoundation.sample.octonaut.xplat.domain.notifications.api

data class NotificationsState(
    val byId: Map<String, Notification> = mapOf(),
    val allIds: List<String> = emptyList()
)

package org.mobilenativefoundation.sample.octonaut.xplat.domain.notifications.api

data class Notification(
    val id: String,
    val unread: Boolean,
    val updatedAt: String,
    val lastReadAt: String?,
    val url: String,
    val subscriptionUrl: String
)

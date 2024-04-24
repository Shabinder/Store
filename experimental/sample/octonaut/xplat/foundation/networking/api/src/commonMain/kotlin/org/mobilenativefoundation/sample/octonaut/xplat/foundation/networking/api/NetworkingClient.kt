package org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api

interface NetworkingClient {
    suspend fun getUser(query: GetUserQuery): GetUserQuery.Data?

    suspend fun listNotifications(queryParams: ListNotificationsQueryParams): ListNotificationsResponse
}
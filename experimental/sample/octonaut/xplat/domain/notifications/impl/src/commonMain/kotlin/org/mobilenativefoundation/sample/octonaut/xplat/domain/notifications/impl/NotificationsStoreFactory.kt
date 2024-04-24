package org.mobilenativefoundation.sample.octonaut.xplat.domain.notifications.impl

import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.domain.notifications.api.NotificationsStore
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.ListNotificationsQueryParams
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.NetworkingClient
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.StoreBuilder

// TODO: Scheduled reverse paging

@Inject
class NotificationsStoreFactory(
    private val networkingClient: NetworkingClient
) {
    fun create(): NotificationsStore {
        val storeBuilder = StoreBuilder.from(
            fetcher = Fetcher.of { queryParams: ListNotificationsQueryParams ->
                networkingClient.listNotifications(queryParams)
            }
        )
        return storeBuilder.build()
    }
}
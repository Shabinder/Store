package org.mobilenativefoundation.sample.octonaut.xplat.domain.feed.impl

import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.domain.feed.api.FeedStore
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.NetworkingClient
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.StoreBuilder

@Inject
class FeedStoreFactory(
    private val networkingClient: NetworkingClient
) {
    fun create(): FeedStore {
        val storeBuilder = StoreBuilder.from(
            fetcher = Fetcher.of { _: Unit ->
                networkingClient.getFeed()
            }
        )

        return storeBuilder.build()
    }
}
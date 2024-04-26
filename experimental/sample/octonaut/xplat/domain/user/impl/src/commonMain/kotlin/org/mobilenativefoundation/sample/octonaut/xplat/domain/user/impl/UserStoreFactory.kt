package org.mobilenativefoundation.sample.octonaut.xplat.domain.user.impl

import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.UserStore
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.GetUserQuery
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.NetworkingClient
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.StoreBuilder

@Inject
class UserStoreFactory(
    private val networkingClient: NetworkingClient,
) {
    fun create(): UserStore {
        val storeBuilder = StoreBuilder.from(
            fetcher = Fetcher.of { query: GetUserQuery ->
                networkingClient.getUser(query)?.user ?: throw IllegalStateException("No user")
            },
        )

        return storeBuilder.build()
    }
}

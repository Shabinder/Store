package org.mobilenativefoundation.sample.octonaut.xplat.domain.repository.impl

import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.domain.repository.api.RepositoryStore
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.GetRepositoryQuery
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.NetworkingClient
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.StoreBuilder

@Inject
class RepositoryStoreFactory(
    private val networkingClient: NetworkingClient
) {
    fun create(): RepositoryStore {
        val storeBuilder = StoreBuilder.from(
            fetcher = Fetcher.of { query: GetRepositoryQuery ->
                networkingClient.getRepository(query)?.repository ?: throw IllegalStateException("Repository not found")
            }
        )
        return storeBuilder.build()
    }
}
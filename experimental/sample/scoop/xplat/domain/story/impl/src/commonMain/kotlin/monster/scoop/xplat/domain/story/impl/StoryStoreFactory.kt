package monster.scoop.xplat.domain.story.impl

import me.tatarka.inject.annotations.Inject
import monster.scoop.xplat.domain.story.api.StoryStore
import monster.scoop.xplat.foundation.networking.api.GetStoryQuery
import monster.scoop.xplat.foundation.networking.api.NetworkingClient
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.FetcherResult
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder

@Inject
class StoryStoreFactory(
    private val networkingClient: NetworkingClient
) : StoreFactory<GetStoryQuery, GetStoryQuery.Data> {
    override fun create(): StoryStore = StoreBuilder
        .from<GetStoryQuery, GetStoryQuery.Data>(
            fetcher = fetcher { networkingClient.getStory(it) }
        )
        .build()
}


interface StoreFactory<Key : Any, Output : Any> {
    fun create(): Store<Key, Output>
}


fun <Key : Any, Network : Any> fetcher(
    getData: suspend (Key) -> Network?
): Fetcher<Key, Network> {

    suspend fun fetch(key: Key): FetcherResult<Network> {
        val data = getData(key)
        return if (data != null) {
            FetcherResult.Data(data)
        } else {
            FetcherResult.Error.Message("Not found")
        }
    }

    return Fetcher.ofResult(::fetch)
}
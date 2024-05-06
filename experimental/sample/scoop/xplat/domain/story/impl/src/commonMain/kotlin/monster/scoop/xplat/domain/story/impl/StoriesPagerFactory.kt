package monster.scoop.xplat.domain.story.impl

import kotlinx.coroutines.CoroutineDispatcher
import me.tatarka.inject.annotations.Inject
import monster.scoop.xplat.common.market.ScoopAction
import monster.scoop.xplat.common.market.ScoopDispatcher
import monster.scoop.xplat.domain.story.api.*
import monster.scoop.xplat.foundation.networking.api.GetStoriesQuery
import monster.scoop.xplat.foundation.networking.api.GetStoryQuery
import monster.scoop.xplat.foundation.networking.api.NetworkingClient
import org.mobilenativefoundation.market.StatefulMarket
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.FetcherResult
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.storex.paging.PagingConfig
import org.mobilenativefoundation.storex.paging.PagingSource
import org.mobilenativefoundation.storex.paging.StoreX


data class PageInfo(
    val count: Int? = null,
    val totalItems: Int? = null,
    val itemsBefore: Int? = null,
    val itemsAfter: Int? = null
)


@Inject
class StoriesPagerFactory(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val networkingClient: NetworkingClient,
    private val marketDispatcher: ScoopDispatcher
) {

    private val pagingConfig = PagingConfig(
        pageSize = 10,
        prefetchDistance = 50
    )

    fun create(): StoriesPager = StoriesPagerBuilder(
        coroutineDispatcher
    )
        .pagingConfig(pagingConfig)
        .pagingSource(
            pageStore = pageStore,
            throwableConverter = { StoriesError.Default.Exception(it) },
            messageConverter = { StoriesError.Default.Message(it) },
            itemStore = itemStore,
            onEachItemStoreResponse = ::onEachItemStoreResponse,
            onEachPagingSourceLoadResult = ::onEachPagingSourceLoadResult
        )
        .defaultFetchingStrategy()
        .pagingOffsetCalculator { key ->
            key.offset + key.limit
        }
        .build(
            placeholderFactory = placeholderFactory,
            keyFactory = keyFactory
        )

    private val placeholderFactory = StoriesPlaceholderFactory {
        StoreX.Paging.Data.Item(NetworkStory.placeholder(), StoreX.Paging.DataSource.PLACEHOLDER)
    }

    private val keyFactory = StoriesKeyFactory { anchorPosition ->
        StoriesPagingKey(
            limit = pagingConfig.maxSize,
            offset = anchorPosition
        )
    }

    private val itemStore = StoriesItemStoreBuilder.from(
        fetcher = Fetcher.ofResult { id: Int ->
            val data = networkingClient.getStory(GetStoryQuery(id))
            val storyFields = data?.stories_by_pk?.storyFields
            if (storyFields != null) {
                val networkStory = NetworkStory(
                    id = storyFields.id,
                    data = storyFields
                )

                FetcherResult.Data(networkStory)
            } else {
                FetcherResult.Error.Message("Not found")
            }
        }
    ).build()


    private val pageStore: StoriesPageStore = StoriesPageStoreBuilder.from(
        fetcher = Fetcher.ofResult { key: StoriesPagingKey ->

            val data = networkingClient.getStories(GetStoriesQuery(key.limit, key.offset))

            if (data != null) {

                val totalItems = data.stories_aggregate.aggregate?.count

                val pageInfo = PageInfo(
                    count = data.stories.size,
                    totalItems = totalItems,
                )

                val origin = StoreX.Paging.DataSource.NETWORK
                val pagingSourceData = StoriesPagingSourceLoadResultData(
                    items = data.stories.map {
                        StoreX.Paging.Data.Item(
                            NetworkStory(it.storyFields.id, it.storyFields),
                            origin = origin
                        )
                    },
                    key = key,
                    nextOffset = data.stories.lastOrNull()?.storyFields?.id, // Offset rather than +1, server is responsible for getting next
                    origin = origin,
                    extras = buildMap {
                        "pageInfo" to pageInfo
                    }
                )

                FetcherResult.Data(pagingSourceData)
            } else {
                FetcherResult.Error.Message("Not found")
            }
        },
    ).build()

    private fun onEachPagingSourceLoadResult(key: StoriesPagingKey, result: StoriesPagingSourceLoadResult) {
        when (result) {
            is PagingSource.LoadResult.Data -> {
                // TODO
            }

            is PagingSource.LoadResult.Error -> {
                // TODO
            }

            is PagingSource.LoadResult.Loading -> {
                // TODO
            }
        }
    }

    // TODO: Handle mutations
    // TODO: Handle data status changes
    private fun onEachItemStoreResponse(id: Int, response: StoreReadResponse<NetworkStory>) {
        val action = when (response) {
            is StoreReadResponse.Data -> ScoopAction.Stories.NormalizeStory(response.value)
            is StoreReadResponse.Error.Custom<*> -> ScoopAction.Stories.SetStory(
                id,
                StatefulMarket.ItemState.Error(StoriesError.Default.Message(response.error.toString()))
            )

            is StoreReadResponse.Error.Exception -> {
                ScoopAction.Stories.SetStory(
                    id,
                    StatefulMarket.ItemState.Error(StoriesError.Default.Exception(response.error))
                )
            }

            is StoreReadResponse.Error.Message -> {
                ScoopAction.Stories.SetStory(
                    id,
                    StatefulMarket.ItemState.Error(StoriesError.Default.Message(response.message))
                )
            }

            StoreReadResponse.Initial -> {
                ScoopAction.Stories.SetStory(
                    id,
                    StatefulMarket.ItemState.Initial(id)
                )
            }

            is StoreReadResponse.Loading -> {
                ScoopAction.Stories.SetStory(
                    id,
                    StatefulMarket.ItemState.Initial(id)
                )
            }

            is StoreReadResponse.NoNewData -> {
                ScoopAction.Stories.UpdateStoryDataStatus(
                    StatefulMarket.ItemState.Data.Status.Idle
                )
            }
        }

        marketDispatcher.dispatch(action)
    }
}



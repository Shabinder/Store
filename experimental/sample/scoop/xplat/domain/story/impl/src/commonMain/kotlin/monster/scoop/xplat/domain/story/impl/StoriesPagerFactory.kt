package monster.scoop.xplat.domain.story.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import monster.scoop.xplat.common.market.ScoopAction
import monster.scoop.xplat.common.market.ScoopDispatcher
import monster.scoop.xplat.domain.story.api.*
import monster.scoop.xplat.foundation.networking.api.GetStoriesQuery
import monster.scoop.xplat.foundation.networking.api.GetStoryQuery
import monster.scoop.xplat.foundation.networking.api.NetworkingClient
import monster.scoop.xplat.foundation.networking.api.fragment.StoryFields
import org.mobilenativefoundation.market.StatefulMarket
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.FetcherResult
import org.mobilenativefoundation.storex.paging.ErrorHandlingStrategy
import org.mobilenativefoundation.storex.paging.PagingConfig
import org.mobilenativefoundation.storex.paging.StoreX

class StoriesMarketPagerFactory(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val marketDispatcher: ScoopDispatcher,
    private val networkingClient: NetworkingClient
) {
    private val pagingConfig = PagingConfig(pageSize = 10, prefetchDistance = 20, initialLoadSize = 20)

    fun create(): StoriesMarketPager = StoriesMarketPager.from(
        coroutineDispatcher = coroutineDispatcher,
        marketDispatcher = marketDispatcher,
        actionFactory = ::actionFactory,
        pagerBuilder = {
            StoriesPagerFactory(coroutineDispatcher, pagingConfig, networkingClient).create()
        }
    )

    private fun actionFactory(pagingState: StoriesPagingState): ScoopAction.Stories = when (pagingState.status) {
        is StoreX.Paging.State.Status.Error -> TODO()
        StoreX.Paging.State.Status.Idle -> {
            ScoopAction.Stories.Paging.SetData(
                stories = pagingState.pagingBuffer.getAllItems().map { it.value },
                lastModified = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                lastRefreshed = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                status = StatefulMarket.PagingState.Data.Status.Idle
            )
        }

        StoreX.Paging.State.Status.Initial -> TODO()
        StoreX.Paging.State.Status.Loading -> TODO()
    }
}

class StoriesPagerFactory(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val pagingConfig: PagingConfig,
    private val networkingClient: NetworkingClient,
) {

    private val placeholderFactory = StoriesPlaceholderFactory {
        StoreX.Paging.Data.Item(Story.placeholder(),)
    }

    private val keyFactory = StoriesKeyFactory { anchorPosition ->
        StoriesPagingKey(
            limit = pagingConfig.maxSize,
            offset = anchorPosition
        )
    }

    private fun StoryFields.Story_thumbnail.toDomainModel(): StoryThumbnail {
        TODO()
    }

    private fun StoryFields.toDomainModel(): Story = Story(
        id = this.id,
        title = this.title,
        description = this.description,
        url = this.url,
        publicationDate = LocalDateTime.parse(this.publication_date.toString()),
        authorId = this.author.hashCode(), // TODO
        storyThumbnailIds = emptyList(), // TODO
        content = "" // TODO
    )

    private fun GetStoriesQuery.Story.toDomainModel(): Story = this.storyFields.toDomainModel()

    private fun GetStoryQuery.Stories_by_pk.toDomainModel(): Story = this.storyFields.toDomainModel()

    private val storiesItemStore = StoriesItemStoreBuilder.from(
        fetcher = Fetcher.ofResult { id: Int ->
            val data = networkingClient.getStory(GetStoryQuery(id))
            val storyFields = data?.stories_by_pk?.storyFields
            if (storyFields != null) {
                FetcherResult.Data(storyFields.toDomainModel())
            } else {
                FetcherResult.Error.Message("Not found")
            }
        }
    ).build()

    private val storiesPageStore = StoriesPageStoreBuilder.from(
        fetcher = Fetcher.ofResult { key: StoriesPagingKey ->
            val data = networkingClient.getStories(GetStoriesQuery(key.limit, key.offset))
            if (data != null) {
                val stories = data.stories.map { it.toDomainModel() }

                val count = data.stories_aggregate.aggregate?.count

                val itemsBefore = stories.first().id // TODO

                val itemsAfter = count?.let { it - itemsBefore }

                val pagingSourceData = StoriesPagingSourceData(
                    items = stories.map { StoreX.Paging.Data.Item(it,) },
                    key = key,
                    nextOffset = stories.last().id, // Offset rather than +1, server is responsible for getting next
                    itemsBefore = itemsBefore,
                    itemsAfter = itemsAfter,
                    origin = StoreX.Paging.DataSource.NETWORK,
                    extras = mapOf()
                )

                FetcherResult.Data(pagingSourceData)
            } else {
                FetcherResult.Error.Message("Not found")
            }
        }

    ).build()

    fun create(): StoriesPager = StoriesPagerBuilder(
        coroutineDispatcher
    )
        .pagingConfig(pagingConfig)

        .pagingSource(
            pageStore = storiesPageStore,
            throwableConverter = { StoriesError.Default.Exception(it) },
            messageConverter = { StoriesError.Default.Message(it) },
            itemStore = storiesItemStore

        )

        .errorHandlingStrategy(ErrorHandlingStrategy.RetryLast(3))

        .build(
            placeholderFactory = placeholderFactory,
            keyFactory = keyFactory
        )
}




package monster.scoop.xplat.domain.story.api

import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.storex.paging.*

typealias StoriesPager = Pager<Int, StoriesPagingKey, Story, StoriesError>
typealias StoriesNetworkPager = Pager<Int, StoriesPagingKey, NetworkStory, StoriesError>
typealias StoriesNetworkPagerBuilder = PagerBuilder<Int, StoriesPagingKey, NetworkStory, StoriesError>
typealias StoriesNetworkPlaceholderFactory = PlaceholderFactory<Int, NetworkStory>


typealias StoriesPagingState = StoreX.Paging.State<Int, StoriesPagingKey, Story, StoriesError>
typealias StoriesPagerBuilder = PagerBuilder<Int, StoriesPagingKey, Story, StoriesError>
typealias StoriesPlaceholderFactory = PlaceholderFactory<Int, Story>
typealias StoriesKeyFactory = KeyFactory<Int, StoriesPagingKey>
typealias StoriesPagingSourceData = PagingSource.LoadResult.Data<Int, StoriesPagingKey, Story, StoriesError>
typealias StoriesNetworkPagingSourceData = PagingSource.LoadResult.Data<Int, StoriesPagingKey, NetworkStory, StoriesError>
typealias StoriesNetworkPagingSourceLoadResult = PagingSource.LoadResult<Int, StoriesPagingKey, NetworkStory, StoriesError>
typealias StoriesPageStore = Store<StoriesPagingKey, StoriesPagingSourceData>
typealias StoriesNetworkPageStore = Store<StoriesPagingKey, StoriesNetworkPagingSourceData>
typealias StoriesNetworkPageStoreBuilder = StoreBuilder<StoriesPagingKey, StoriesNetworkPagingSourceData>
typealias StoriesPageStoreBuilder = StoreBuilder<StoriesPagingKey, StoriesPagingSourceData>


typealias StoriesItemStore = Store<Int, Story>
typealias StoriesNetworkItemStore = Store<Int, NetworkStory>

typealias StoriesItemStoreBuilder = StoreBuilder<Int, Story>
typealias StoriesNetworkItemStoreBuilder = StoreBuilder<Int, NetworkStory>
typealias StoriesFetchingStrategy = FetchingStrategy<Int, StoriesPagingKey, Story, StoriesError>
typealias StoriesNetworkFetchingStrategy = FetchingStrategy<Int, StoriesPagingKey, NetworkStory, StoriesError>
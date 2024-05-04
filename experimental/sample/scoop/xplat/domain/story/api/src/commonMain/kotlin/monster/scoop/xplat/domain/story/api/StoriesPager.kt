package monster.scoop.xplat.domain.story.api

import org.mobilenativefoundation.market.MarketPager
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.storex.paging.*

typealias StoriesPager = Pager<Int, StoriesPagingKey, Story, StoriesError>
typealias StoriesMarketPager = MarketPager<Int>
typealias StoriesPagingState = StoreX.Paging.State<Int, StoriesPagingKey, Story, StoriesError>
typealias StoriesPagerBuilder = PagerBuilder<Int, StoriesPagingKey, Story, StoriesError>
typealias StoriesPlaceholderFactory = PlaceholderFactory<Int, Story>
typealias StoriesKeyFactory = KeyFactory<Int, StoriesPagingKey>
typealias StoriesPagingSourceData = PagingSource.LoadResult.Data<Int, StoriesPagingKey, Story, StoriesError>
typealias StoriesPageStore = Store<StoriesPagingKey, StoriesPagingSourceData>
typealias StoriesPageStoreBuilder = StoreBuilder<StoriesPagingKey, StoriesPagingSourceData>

typealias StoriesItemStore = Store<Int, Story>
typealias StoriesItemStoreBuilder = StoreBuilder<Int, Story>
typealias StoriesFetchingStrategy = FetchingStrategy<Int, StoriesPagingKey, Story, StoriesError>
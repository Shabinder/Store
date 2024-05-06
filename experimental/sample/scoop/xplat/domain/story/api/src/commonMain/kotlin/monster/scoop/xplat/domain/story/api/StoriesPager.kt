package monster.scoop.xplat.domain.story.api

import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.storex.paging.*

typealias StoriesPager = Pager<Int, StoriesPagingKey, NetworkStory, StoriesError>
typealias StoriesPagerBuilder = PagerBuilder<Int, StoriesPagingKey, NetworkStory, StoriesError>
typealias StoriesPlaceholderFactory = PlaceholderFactory<Int, NetworkStory>
typealias StoriesKeyFactory = KeyFactory<Int, StoriesPagingKey>
typealias StoriesPagingSourceLoadResultData = PagingSource.LoadResult.Data<Int, StoriesPagingKey, NetworkStory, StoriesError>
typealias StoriesPagingSourceLoadResult = PagingSource.LoadResult<Int, StoriesPagingKey, NetworkStory, StoriesError>
typealias StoriesPageStore = Store<StoriesPagingKey, StoriesPagingSourceLoadResultData>
typealias StoriesPageStoreBuilder = StoreBuilder<StoriesPagingKey, StoriesPagingSourceLoadResultData>
typealias StoriesItemStore = Store<Int, NetworkStory>
typealias StoriesItemStoreBuilder = StoreBuilder<Int, NetworkStory>
typealias StoriesFetchingStrategy = FetchingStrategy<Int, StoriesPagingKey, NetworkStory, StoriesError>
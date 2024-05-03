package org.mobilenativefoundation.storex.paging

/**
 * Represents a strategy for determining whether to fetch more data based on the current state of the pager.
 * The fetching strategy is responsible for deciding whether to fetch more data based on the anchor position,
 * prefetch position, paging configuration, and the current state of the paging buffer.
 *
 * Implementing a custom [FetchingStrategy] allows you to define your own logic for when to fetch more data.
 * For example, you can fetch more data when the user scrolls near the end of the currently loaded data, or when a certain number of items are remaining in the buffer.
 */
interface FetchingStrategy<Id : Comparable<Id>, K : StoreXPaging.Key, V : Identifiable<Id>, E: StoreXPaging.Error> {

    /**
     * Determines whether to fetch more data based on the current state of the pager.
     * The [shouldFetch] implementation should determine whether more data should be fetched based on the provided parameters.
     */
    fun shouldFetch(
        params: PagingSource.LoadParams<K>,
        pagingState: StoreXPaging.State<Id, K, V, E>,
        pagingConfig: PagingConfig,
    ): Boolean
}
package org.mobilenativefoundation.storex.paging

/**
 * Represents a strategy for aggregating loaded pages of data into a single instance of [StoreXPaging.Items].
 */
interface AggregatingStrategy<Id : Comparable<Id>, K : StoreXPaging.Key, V : Identifiable<Id>, E : StoreXPaging.Error> {
    fun aggregate(
        pagingBuffer: PagingBuffer<Id, K, V, E>,
        anchorPosition: K?,
        prefetchPosition: K?
    ): StoreXPaging.Items<Id, V>
}
package org.mobilenativefoundation.storex.paging

/**
 * Represents a strategy for aggregating loaded pages of data into a single instance of [StoreX.Paging.Items].
 */
interface AggregatingStrategy<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any> {
    fun aggregate(
        pagingBuffer: PagingBuffer<Id, K, V, E>,
        anchorPosition: K?,
        prefetchPosition: K?
    ): StoreX.Paging.Items<Id, V>
}
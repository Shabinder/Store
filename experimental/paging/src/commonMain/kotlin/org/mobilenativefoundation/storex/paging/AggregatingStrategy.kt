package org.mobilenativefoundation.storex.paging

/**
 * Represents a strategy for aggregating loaded pages of data into a single instance of [StoreX.Paging.AggregatedItems].
 */
interface AggregatingStrategy<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any> {
    fun aggregate(
        state: StoreX.Paging.State<Id, K, V, E>
    ): StoreX.Paging.AggregatedItems<Id, V>
}
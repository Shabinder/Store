package org.mobilenativefoundation.storex.paging

/**
 * A strategy for removing duplicate items from the paged data based on a specific criteria.
 */
fun interface DeduplicationStrategy<Id : Comparable<Id>, V : Identifiable<Id>> {
    operator fun invoke(data: List<StoreX.Paging.Data.Item<Id, V>>): List<StoreX.Paging.Data.Item<Id, V>>
}
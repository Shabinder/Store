package org.mobilenativefoundation.storex.paging

/**
 * A strategy for sorting the paged data based on specific sorting parameters.
 */
fun interface SortingStrategy<Id : Comparable<Id>, V : Identifiable<Id>, S : Any> {
    operator fun invoke(data: List<StoreXPaging.Data.Item<Id, V>>, sortingParams: S): List<StoreXPaging.Data.Item<Id, V>>
}
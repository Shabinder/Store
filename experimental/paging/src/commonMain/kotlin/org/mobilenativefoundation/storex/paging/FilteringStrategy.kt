package org.mobilenativefoundation.storex.paging

/**
 * A strategy for filtering the paged data based on specific filtering parameters.
 */
fun interface FilteringStrategy<Id : Comparable<Id>, V : Identifiable<Id>, F: Any> {
    operator fun invoke(data: List<StoreXPaging.Data.Item<Id, V>>, filterParams: F): List<StoreXPaging.Data.Item<Id, V>>
}
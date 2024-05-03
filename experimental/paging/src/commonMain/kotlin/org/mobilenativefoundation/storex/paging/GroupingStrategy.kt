package org.mobilenativefoundation.storex.paging

/**
 * A strategy for grouping the paged data based on a specific criteria or field.
 */
fun interface GroupingStrategy<Id : Comparable<Id>, V : Identifiable<Id>, G: Any> {
    operator fun invoke(data: List<StoreX.Paging.Data.Item<Id, V>>, params: G): Map<G, List<StoreX.Paging.Data.Item<Id, V>>>
}
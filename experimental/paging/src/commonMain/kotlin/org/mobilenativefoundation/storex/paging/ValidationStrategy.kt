package org.mobilenativefoundation.storex.paging

/**
 * A strategy for validating the paged data against specific rules or constraints.
 */
fun interface ValidationStrategy<Id : Comparable<Id>, V : Identifiable<Id>> {
    /**
     * Validates the paged data against specific rules or constraints.
     */
    operator fun invoke(data: List<StoreX.Paging.Data.Item<Id, V>>): List<StoreX.Paging.Data.Item<Id, V>>
}
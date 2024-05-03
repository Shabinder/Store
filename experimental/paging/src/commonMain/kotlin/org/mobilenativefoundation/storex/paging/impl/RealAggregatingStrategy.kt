package org.mobilenativefoundation.storex.paging.impl

import org.mobilenativefoundation.storex.paging.*


@Suppress("UNCHECKED_CAST")
class RealAggregatingStrategy<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
    private val operations: List<Operation<Id, K, V>>,
    private val pagingConfig: PagingConfig,
) : AggregatingStrategy<Id, K, V, E> {

    override fun aggregate(
        pagingBuffer: PagingBuffer<Id, K, V, E>,
        anchorPosition: K?,
        prefetchPosition: K?
    ): StoreX.Paging.Items<Id, V> {
        if (pagingBuffer.isEmpty()) return StoreX.Paging.Items(emptyList())

        val orderedItems = mutableListOf<StoreX.Paging.Data.Item<Id, V>>()

        var currentPage = pagingBuffer.head()

        fun loadItemsFromPagingBuffer(page: StoreX.Paging.Data.Page<Id, K, V>) =
            page.items.mapNotNull { itemId -> pagingBuffer.get(itemId) }

        while (currentPage != null) {
            when (pagingConfig.insertionStrategy) {
                InsertionStrategy.APPEND -> orderedItems.addAll(loadItemsFromPagingBuffer(currentPage))
                InsertionStrategy.PREPEND -> orderedItems.addAll(0, loadItemsFromPagingBuffer(currentPage))
                InsertionStrategy.REPLACE -> orderedItems.replaceWith(loadItemsFromPagingBuffer(currentPage))
            }

            currentPage = currentPage.nextKey?.let { pagingBuffer.get(it) }
        }

        val nextOrderedItems = applyOperations(orderedItems)

        return StoreX.Paging.Items(nextOrderedItems)
    }

    private fun applyOperations(
        orderedItems: MutableList<StoreX.Paging.Data.Item<Id, V>>,
    ): MutableList<StoreX.Paging.Data.Item<Id, V>> {

        operations.forEach { operation ->
            val nextOrderedItems = when (operation) {
                is Operation.Deduplicate -> operation.strategy(orderedItems)
                is Operation.Filter<*, *, *, *> -> {
                    operation as Operation.Filter<Id, K, V, Any>
                    operation.strategy(orderedItems, operation.params)
                }

                is Operation.Group<*, *, *, *> -> {
                    operation as Operation.Group<Id, K, V, Any>
                    operation.strategy(orderedItems, operation.params).values.flatten()
                }

                is Operation.Sort<*, *, *, *> -> {
                    operation as Operation.Sort<Id, K, V, Any>
                    operation.strategy(orderedItems, operation.params)
                }

                is Operation.Transform -> operation.strategy(orderedItems)
                is Operation.Validate -> operation.strategy(orderedItems)
            }

            orderedItems.replaceWith(nextOrderedItems)
        }

        return orderedItems
    }

    private fun MutableList<StoreX.Paging.Data.Item<Id, V>>.replaceWith(
        items: List<StoreX.Paging.Data.Item<Id, V>>
    ) = apply {
        clear()
        addAll(items)
    }

}
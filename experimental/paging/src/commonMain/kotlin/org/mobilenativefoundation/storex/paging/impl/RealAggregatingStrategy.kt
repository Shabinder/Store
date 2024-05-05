package org.mobilenativefoundation.storex.paging.impl

import org.mobilenativefoundation.storex.paging.*


@Suppress("UNCHECKED_CAST")
class RealAggregatingStrategy<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
    private val operations: List<Operation<Id, K, V>>,
    private val pagingConfig: PagingConfig,
    private val placeholderFactory: PlaceholderFactory<Id, V>
) : AggregatingStrategy<Id, K, V, E> {

    override fun aggregate(state: StoreX.Paging.State<Id, K, V, E>): StoreX.Paging.AggregatedItems<Id, V> {

        if (state.pagingBuffer.isEmpty()) return StoreX.Paging.AggregatedItems()


        val orderedItems = mutableListOf<StoreX.Paging.Data.Item<Id, V>>()

        var currentPage = state.pagingBuffer.head()

        fun loadItemsFromPagingBuffer(page: StoreX.Paging.Data.Page<Id, K, V>): List<StoreX.Paging.Data.Item<Id, V>> {

            if (page.origin == StoreX.Paging.DataSource.PLACEHOLDER) {
                return List(pagingConfig.pageSize) { placeholderFactory.invoke() }
            }

            val newItems = page.items.mapNotNull { itemId -> state.pagingBuffer.get(itemId) }
            return newItems
        }


        while (currentPage != null) {
            when (pagingConfig.insertionStrategy) {
                InsertionStrategy.APPEND -> orderedItems.addAll(loadItemsFromPagingBuffer(currentPage))
                InsertionStrategy.PREPEND -> orderedItems.addAll(0, loadItemsFromPagingBuffer(currentPage))
                InsertionStrategy.REPLACE -> orderedItems.replaceWith(loadItemsFromPagingBuffer(currentPage))
            }


            val nextPage = state.pagingBuffer.getNextPage(currentPage)

            currentPage = nextPage
        }


        val nextOrderedItems = applyOperations(orderedItems)


        return StoreX.Paging.AggregatedItems(nextOrderedItems)
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
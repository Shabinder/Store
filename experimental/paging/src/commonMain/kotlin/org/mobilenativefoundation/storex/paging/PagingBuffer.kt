package org.mobilenativefoundation.storex.paging

/**
 * A custom data structure for efficiently storing and retrieving paging data.
 * The [PagingBuffer] is responsible for caching and providing access to the loaded pages of data.
 * It allows retrieving data by load parameters, page key, or accessing the entire buffer.
 */
interface PagingBuffer<Id : Comparable<Id>, K : StoreXPaging.Key, V : Identifiable<Id>, E : StoreXPaging.Error> {
    fun get(params: PagingSource.LoadParams<K>): StoreXPaging.Data.Page<Id, K, V>?

    fun get(key: K): StoreXPaging.Data.Page<Id, K, V>?

    fun get(id: Id): StoreXPaging.Data.Item<Id, V>?

    fun head(): StoreXPaging.Data.Page<Id, K, V>?

    fun getAll(): List<StoreXPaging.Data.Page<Id, K, V>>

    fun isEmpty(): Boolean

    fun indexOf(key: K): Int

    fun getItemsInRange(
        anchorPosition: K,
        prefetchPosition: K?,
        pagingConfig: PagingConfig
    ): List<StoreXPaging.Data.Item<Id, V>>
}
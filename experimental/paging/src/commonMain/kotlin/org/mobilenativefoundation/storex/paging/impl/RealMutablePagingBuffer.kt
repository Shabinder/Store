package org.mobilenativefoundation.storex.paging.impl

import org.mobilenativefoundation.storex.paging.*


class RealMutablePagingBuffer<Id : Comparable<Id>, K : StoreXPaging.Key, V : Identifiable<Id>, E : StoreXPaging.Error>(
    private val maxSize: Int = 500
) : MutablePagingBuffer<Id, K, V, E> {

    private val pages: Array<StoreXPaging.Data.Page<Id, K, V>?> =
        arrayOfNulls(maxSize)
    private val paramsToIndex: MutableMap<PagingSource.LoadParams<K>, Int> = mutableMapOf()
    private val keyToIndex: MutableMap<K, Int> = mutableMapOf()
    private val items: MutableMap<Id, StoreXPaging.Data.Item<Id, V>> = mutableMapOf()

    private var head = 0
    private var tail = 0
    private var size = 0

    override fun get(params: PagingSource.LoadParams<K>): StoreXPaging.Data.Page<Id, K, V>? {
        val index = paramsToIndex[params]
        return if (index != null) pages[index] else null
    }

    override fun get(key: K): StoreXPaging.Data.Page<Id, K, V>? {
        val index = keyToIndex[key]
        return if (index != null) pages[index] else null
    }

    private fun computeIndex(key: K): Int {
        keyToIndex[key]?.let {
            return it
        }

        if (size == maxSize) {
            val oldestIndex = head
            val oldestParams = paramsToIndex.entries.first { it.value == oldestIndex }.key
            paramsToIndex.remove(oldestParams)
            keyToIndex.remove(oldestParams.key)
            pages[oldestIndex] = null
            head = (head + 1) % maxSize
        }
        return tail
    }

    override fun put(
        params: PagingSource.LoadParams<K>,
        page: PagingSource.LoadResult.Data<Id, K, V, E>
    ) {
        val index = computeIndex(params.key)
        pages[index] = StoreXPaging.Data.Page(
            items = page.items.map { it.value.id },
            key = page.key,
            nextKey = page.nextKey,
            itemsBefore = page.itemsBefore,
            itemsAfter = page.itemsAfter,
            origin = page.origin,
            extras = page.extras
        )
        paramsToIndex[params] = index
        keyToIndex[params.key] = index
        tail = (tail + 1) % maxSize
        size = minOf(size + 1, maxSize)

        page.items.forEach { item ->
            items[item.value.id] = item
        }
    }

    override fun get(id: Id): StoreXPaging.Data.Item<Id, V>? {
        return items[id]
    }

    override fun head(): StoreXPaging.Data.Page<Id, K, V>? {
        return pages[head]
    }

    override fun getAll(): List<StoreXPaging.Data.Page<Id, K, V>> {
        val pages = mutableListOf<StoreXPaging.Data.Page<Id, K, V>>()
        var index = head
        var count = 0
        while (count < size) {
            val page = this.pages[index]
            if (page != null) {
                pages.add(page)
            }
            index = (index + 1) % maxSize
            count++
        }
        return pages
    }

    override fun isEmpty(): Boolean = size == 0

    override fun indexOf(key: K): Int {
        return keyToIndex[key] ?: -1
    }

    override fun getItemsInRange(
        anchorPosition: K,
        prefetchPosition: K?,
        pagingConfig: PagingConfig
    ): List<StoreXPaging.Data.Item<Id, V>> {
        val buffer = getAll()

        val anchorIndex = indexOf(anchorPosition)

        if (anchorIndex == -1) {
            return emptyList()
        }

        val prefetchIndex = prefetchPosition?.let { indexOf(it) } ?: buffer.lastIndex

        val startIndex = (anchorIndex - pagingConfig.prefetchDistance).coerceAtLeast(0)
        val endIndex =
            (prefetchIndex + pagingConfig.prefetchDistance).coerceAtMost(buffer.lastIndex)

        return buffer.subList(startIndex, endIndex + 1)
            .flatMap { page -> page.items.mapNotNull { itemId -> items[itemId] } }
    }
}
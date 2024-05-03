package org.mobilenativefoundation.storex.paging

/**
 * Represents a manager for the queue of pages to be loaded.
 */
interface QueueManager<K : StoreXPaging.Key> {
    /**
     * Enqueues a page key to be loaded.
     */
    fun enqueue(params: PagingSource.LoadParams<K>)
}
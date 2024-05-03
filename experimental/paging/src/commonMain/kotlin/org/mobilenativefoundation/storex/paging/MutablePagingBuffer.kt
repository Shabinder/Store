package org.mobilenativefoundation.storex.paging

/**
 * Represents a mutable version of [PagingBuffer] that allows adding and updating paging data.
 */
interface MutablePagingBuffer<Id : Comparable<Id>, K : StoreXPaging.Key, V : Identifiable<Id>, E: StoreXPaging.Error> :
    PagingBuffer<Id, K, V, E> {

    /**
     * Puts the loaded page of data associated with the specified [PagingSource.LoadParams] into the buffer.
     */
    fun put(params: PagingSource.LoadParams<K>, page: PagingSource.LoadResult.Data<Id, K, V, E>)
}
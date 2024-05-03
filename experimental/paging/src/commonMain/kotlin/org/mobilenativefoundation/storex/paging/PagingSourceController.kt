package org.mobilenativefoundation.storex.paging


interface PagingSourceController<K : StoreXPaging.Key> {
    fun lazyLoad(params: PagingSource.LoadParams<K>)
}
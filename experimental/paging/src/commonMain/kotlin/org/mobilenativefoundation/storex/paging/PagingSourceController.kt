package org.mobilenativefoundation.storex.paging


interface PagingSourceController<K : Any> {
    fun lazyLoad(params: PagingSource.LoadParams<K>)
}
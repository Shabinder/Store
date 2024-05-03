package org.mobilenativefoundation.storex.paging

fun interface Middleware<K : Any> {
    suspend fun apply(
        params: PagingSource.LoadParams<K>,
        next: suspend (params: PagingSource.LoadParams<K>) -> PagingSource.LoadParams<K>
    ): PagingSource.LoadParams<K>
}
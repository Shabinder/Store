package org.mobilenativefoundation.storex.paging.impl

import kotlinx.coroutines.flow.Flow
import org.mobilenativefoundation.storex.paging.Identifiable
import org.mobilenativefoundation.storex.paging.PagingSource

interface PagingSourceStreamProvider<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any> {
    operator fun invoke(params: PagingSource.LoadParams<K>): Flow<PagingSource.LoadResult<Id, K, V, E>>
}
package org.mobilenativefoundation.storex.paging.impl

import kotlinx.coroutines.flow.Flow
import org.mobilenativefoundation.storex.paging.*

interface PagingSourceStreamProvider<Id : Comparable<Id>, K : StoreXPaging.Key, V : Identifiable<Id>, E : StoreXPaging.Error> {
    operator fun invoke(params: PagingSource.LoadParams<K>): Flow<PagingSource.LoadResult<Id, K, V, E>>
}
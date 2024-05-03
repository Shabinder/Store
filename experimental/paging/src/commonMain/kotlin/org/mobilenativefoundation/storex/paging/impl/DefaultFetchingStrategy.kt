package org.mobilenativefoundation.storex.paging.impl

import org.mobilenativefoundation.storex.paging.*
import kotlin.math.max

class DefaultFetchingStrategy<Id : Comparable<Id>, K : StoreXPaging.Key, V : Identifiable<Id>, E : StoreXPaging.Error> :
    FetchingStrategy<Id, K, V, E> {
    override fun shouldFetch(
        params: PagingSource.LoadParams<K>,
        pagingState: StoreXPaging.State<Id, K, V, E>,
        pagingConfig: PagingConfig,
    ): Boolean {

        pagingState.anchorPosition?.let { anchorPosition ->
            pagingState.prefetchPosition?.let { prefetchPosition ->

                val indexOfAnchor = pagingState.pagingBuffer.indexOf(anchorPosition)
                val indexOfPrefetch = pagingState.pagingBuffer.indexOf(prefetchPosition)

                if ((indexOfAnchor == -1 && indexOfPrefetch == -1) || indexOfPrefetch == -1) return true

                val effectiveAnchor = max(indexOfAnchor, 0)
                val effectivePrefetch = (indexOfPrefetch + 1) * pagingConfig.pageSize

                return effectivePrefetch - effectiveAnchor < pagingConfig.prefetchDistance
            }
        }

        return true
    }
}
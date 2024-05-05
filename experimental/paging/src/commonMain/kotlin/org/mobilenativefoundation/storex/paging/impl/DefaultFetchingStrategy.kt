package org.mobilenativefoundation.storex.paging.impl

import org.mobilenativefoundation.storex.paging.*

class DefaultFetchingStrategy<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
    private val pagedItemDistanceCalculator: PagedItemDistanceCalculator<Id>? = null
) :
    FetchingStrategy<Id, K, V, E> {
    override fun shouldFetch(
        params: PagingSource.LoadParams<K>,
        fetchingState: FetchingState<Id>,
        pagingState: StoreX.Paging.State<Id, K, V, E>,
        pagingConfig: PagingConfig,
    ): Boolean {

        fetchingState.anchorPosition?.let { anchorPosition ->
            fetchingState.prefetchPosition?.let { prefetchPosition ->

                val distance = pagedItemDistanceCalculator?.calculate(anchorPosition, prefetchPosition)
                    ?: pagingState.pagingBuffer.minDistanceBetween(anchorPosition, prefetchPosition)


                return distance < pagingConfig.prefetchDistance
            }
        }

        return true
    }
}
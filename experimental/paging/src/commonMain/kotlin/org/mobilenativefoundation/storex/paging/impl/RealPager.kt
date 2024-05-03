package org.mobilenativefoundation.storex.paging.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.mobilenativefoundation.storex.paging.*


class RealPager<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
    dispatcher: CoroutineDispatcher,
    pagingStateProvider: PagingStateProvider<Id, K, V, E>,
    private val loader: Loader<Id, K, V, E>,
    private val launchEffects: List<LaunchEffect>,
    private val aggregatingStrategy: AggregatingStrategy<Id, K, V, E>
) : Pager<Id, K, V, E> {

    private val coroutineScope = CoroutineScope(dispatcher)

    init {
        coroutineScope.launch {
            launchEffects.forEach { it() }
        }
    }

    override operator fun invoke(anchorPosition: Flow<Id>) {
        coroutineScope.launch {
            anchorPosition.collectLatest { anchorPosition ->
                loader(anchorPosition)
            }
        }
    }

    override val state: StateFlow<StoreX.Paging.State<Id, K, V, E>> = pagingStateProvider.stateFlow()

    override val pagingItems: Flow<StoreX.Paging.Items<Id, V>> =
        state.filterIsInstance<StoreX.Paging.State.Data<Id, K, V, E>>().map { dataState ->
            aggregatingStrategy.aggregate(
                dataState.pagingBuffer,
                dataState.anchorPosition,
                dataState.prefetchPosition
            )
        }

    override val flow: Flow<StoreX.Paging.State<Id, K, V, E>> = pagingStateProvider.stateFlow()
}
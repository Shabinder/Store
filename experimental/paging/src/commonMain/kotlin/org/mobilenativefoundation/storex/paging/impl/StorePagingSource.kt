package org.mobilenativefoundation.storex.paging.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.mobilenativefoundation.storex.paging.Identifiable
import org.mobilenativefoundation.storex.paging.PagingSource

class StorePagingSource<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
    dispatcher: CoroutineDispatcher,
    private val streamProvider: StorePagingSourceStreamProvider<Id, K, V, E>,
) : PagingSource<Id, K, V, E> {

    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcher)

    private val streams = mutableMapOf<K, Flow<PagingSource.LoadResult<Id, K, V, E>>>()
    override fun load(
        params: PagingSource.LoadParams<K>,
        onStateTransition: PagingSource.OnStateTransition<Id, K, V, E>
    ) {
        if (params.key !in streams) {
            val flow = streamProvider(params)

            coroutineScope.launch {
                flow.collect { loadResult ->
                    when (loadResult) {
                        is PagingSource.LoadResult.Data -> {
                            onStateTransition.onData(loadResult)
                        }

                        is PagingSource.LoadResult.Error -> {
                            onStateTransition.onError(loadResult)
                        }

                        is PagingSource.LoadResult.Loading -> {
                            onStateTransition.onLoading()
                        }
                    }
                }
            }

            streams[params.key] = flow
        }
    }
}
package org.mobilenativefoundation.storex.paging.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.StoreReadResponseOrigin
import org.mobilenativefoundation.storex.paging.Identifiable
import org.mobilenativefoundation.storex.paging.PagingSource
import org.mobilenativefoundation.storex.paging.StoreX

class StorePagingSourceStreamProvider<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
    private val createPageStream: (PagingSource.LoadParams<K>) -> Flow<PagingSource.LoadResult<Id, K, V, E>>,
    private val createItemStream: ((Id) -> Flow<StoreReadResponse<V>>)?,
) : PagingSourceStreamProvider<Id, K, V, E> {
    private val loadResultsData: MutableMap<K, PagingSource.LoadResult.Data<Id, K, V, E>> =
        mutableMapOf()
    private val mutexForPages = Mutex()

    override fun invoke(params: PagingSource.LoadParams<K>): Flow<PagingSource.LoadResult<Id, K, V, E>> {
        return createPageStream(params).map { result ->
            when (result) {
                is PagingSource.LoadResult.Data -> {
                    mutexForPages.withLock {
                        this.loadResultsData[params.key] = result
                    }

                    var liveLoadResultData = result

                    createItemStream?.let { createItemStream ->
                        result.items.forEachIndexed { index, item ->
                            initAndCollectItemStream(
                                item.value,
                                index,
                                params.key,
                                createItemStream
                            ) { updatedData ->
                                liveLoadResultData = updatedData
                            }
                        }
                    }

                    liveLoadResultData
                }

                is PagingSource.LoadResult.Error,
                is PagingSource.LoadResult.Loading -> result
            }
        }
    }

    private fun initAndCollectItemStream(
        data: V,
        index: Int,
        parentKey: K,
        createItemStream: (Id) -> Flow<StoreReadResponse<V>>,
        emit: (PagingSource.LoadResult.Data<Id, K, V, E>) -> Unit
    ) {
        createItemStream(data.id).distinctUntilChanged().onEach { response ->
            if (response is StoreReadResponse.Data) {
                val updatedValue = response.value

                mutexForPages.withLock {
                    this.loadResultsData[parentKey]!!.let { currentData ->

                        val updatedItems = currentData.items.toMutableList()
                        val item = updatedItems[index]
                        if (item != updatedValue) {

                            // TODO : Move to util
                            val origin = when (response.origin) {
                                StoreReadResponseOrigin.Cache -> StoreX.Paging.DataSource.MEMORY_CACHE
                                is StoreReadResponseOrigin.Fetcher -> StoreX.Paging.DataSource.NETWORK
                                StoreReadResponseOrigin.Initial -> StoreX.Paging.DataSource.PLACEHOLDER
                                StoreReadResponseOrigin.SourceOfTruth -> StoreX.Paging.DataSource.SOURCE_OF_TRUTH
                            }
                            updatedItems[index] = StoreX.Paging.Data.Item(updatedValue, origin)
                            val updatedPage = currentData.copy(items = updatedItems)
                            this.loadResultsData[parentKey] = updatedPage
                            emit(updatedPage)
                        }
                    }
                }
            }
        }
    }
}
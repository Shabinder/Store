package org.mobilenativefoundation.storex.paging.impl

import org.mobilenativefoundation.storex.paging.*


class RealPagingSourceController<Id : Comparable<Id>, K : StoreXPaging.Key, V : Identifiable<Id>, E : StoreXPaging.Error>(
    private val sideEffects: List<SideEffect<Id, K, V, E>>,
    private val pagingConfig: PagingConfig,
    private val jobCoordinator: JobCoordinator,
    private val pagingSource: PagingSource<Id, K, V, E>,
    private val pagingStateManager: PagingStateManager<Id, K, V, E>,
    private val retriesManager: RetriesManager<K>,
) : PagingSourceController<K> {

    override fun lazyLoad(params: PagingSource.LoadParams<K>) {
        jobCoordinator.launchIfNotActive(params) {
            pagingSource.load(params, onStateTransition = createOnStateTransition(params))
        }
    }

    private suspend fun onLoading(params: PagingSource.LoadParams<K>) {
        pagingStateManager.update { prevState ->
            when (prevState) {
                is StoreXPaging.State.Data -> {
                    StoreXPaging.State.LoadingMore(
                        pagingBuffer = prevState.pagingBuffer,
                        anchorPosition = params.key,
                        prefetchPosition = prevState.prefetchPosition
                    )
                }

                is StoreXPaging.State.Initial,
                is StoreXPaging.State.Loading,
                is StoreXPaging.State.Error -> {
                    StoreXPaging.State.Loading(
                        pagingBuffer = prevState.pagingBuffer,
                        anchorPosition = params.key,
                        prefetchPosition = prevState.prefetchPosition
                    )
                }
            }
        }
    }

    private suspend fun onData(
        params: PagingSource.LoadParams<K>,
        data: PagingSource.LoadResult.Data<Id, K, V, E>
    ) {
        retriesManager.resetFor(params)

        pagingStateManager.mutate { mutablePagingBuffer ->
            mutablePagingBuffer.put(params, data)
        }

        pagingStateManager.update { prevState ->

            val nextState = StoreXPaging.State.Idle(
                pagingBuffer = prevState.pagingBuffer,
                anchorPosition = prevState.anchorPosition,
                prefetchPosition = prevState.prefetchPosition,
            )


            nextState
        }


        launchSideEffects(pagingStateManager.getState())
    }

    private suspend fun onError(
        params: PagingSource.LoadParams<K>,
        error: PagingSource.LoadResult.Error<Id, K, V, E>
    ) {

        suspend fun passErrorThroughAndLaunchSideEffects() {
            pagingStateManager.update { prevState ->
                when (prevState) {
                    is StoreXPaging.State.ErrorLoadingMore -> {
                        StoreXPaging.State.ErrorLoadingMore(
                            pagingBuffer = prevState.pagingBuffer,
                            error = error.value,
                            anchorPosition = prevState.anchorPosition,
                            prefetchPosition = prevState.prefetchPosition
                        )
                    }

                    is StoreXPaging.State.Data -> {
                        StoreXPaging.State.ErrorLoadingMore(
                            pagingBuffer = prevState.pagingBuffer,
                            error = error.value,
                            anchorPosition = prevState.anchorPosition,
                            prefetchPosition = prevState.prefetchPosition
                        )
                    }

                    is StoreXPaging.State.Initial,
                    is StoreXPaging.State.Loading,
                    is StoreXPaging.State.Error -> {
                        StoreXPaging.State.Error(
                            value = error.value,
                            pagingBuffer = prevState.pagingBuffer,
                            anchorPosition = prevState.anchorPosition,
                            prefetchPosition = prevState.prefetchPosition
                        )
                    }
                }
            }

            launchSideEffects(pagingStateManager.getState())
        }


        when (val errorHandlingStrategy = pagingConfig.errorHandlingStrategy) {
            ErrorHandlingStrategy.Ignore -> {
                // Ignore
            }

            ErrorHandlingStrategy.PassThrough -> passErrorThroughAndLaunchSideEffects()

            is ErrorHandlingStrategy.RetryLast -> {
                val retryCount = retriesManager.getCountFor(params)

                if (retryCount < errorHandlingStrategy.maxRetries) {
                    jobCoordinator.cancel(params.key)
                    retriesManager.incrementFor(params)
                    jobCoordinator.launch(params) {
                        pagingSource.load(params, createOnStateTransition(params))
                    }
                } else {
                    retriesManager.resetFor(params)
                    passErrorThroughAndLaunchSideEffects()
                }
            }
        }
    }

    private fun createOnStateTransition(params: PagingSource.LoadParams<K>) =
        PagingSource.OnStateTransition<Id, K, V, E>(
            onLoading = {
                onLoading(params)
            },
            onError = { error ->
                onError(params, error)
            },
            onData = { data ->
                onData(params, data)
            }
        )

    private fun launchSideEffects(state: StoreXPaging.State<Id, K, V, E>) {
        sideEffects.forEach { it(state) }
    }
}
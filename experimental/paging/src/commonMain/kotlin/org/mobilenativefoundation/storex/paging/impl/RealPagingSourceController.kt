package org.mobilenativefoundation.storex.paging.impl

import org.mobilenativefoundation.storex.paging.*


class RealPagingSourceController<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
    private val sideEffects: List<SideEffect<Id, K, V, E>>,
    private val pagingConfig: PagingConfig,
    private val jobCoordinator: JobCoordinator,
    private val pagingSource: PagingSource<Id, K, V, E>,
    private val pagingStateManager: PagingStateManager<Id, K, V, E>,
    private val retriesManager: RetriesManager<K>,
    loaderInjector: Injector<Loader<Id, K, V, E>>
) : PagingSourceController<K> {

    private val loader = lazy { loaderInjector.require() }

    override fun lazyLoad(params: PagingSource.LoadParams<K>) {
        jobCoordinator.launchIfNotActive(params) {
            println("LAUNCHING")
            pagingSource.load(params, onStateTransition = createOnStateTransition(params))
        }
    }

    private suspend fun onLoading() {
        pagingStateManager.update { prevState ->
            prevState.copy(status = StoreX.Paging.State.Status.Loading)
        }
    }

    private suspend fun onData(
        params: PagingSource.LoadParams<K>,
        data: PagingSource.LoadResult.Data<Id, K, V, E>
    ) {
        println("ON DATA, item count: ${data.items.size}")
        retriesManager.resetFor(params)

        pagingStateManager.mutate { mutablePagingBuffer ->
            mutablePagingBuffer.put(params, data)
            println("Updated paging buffer: ${mutablePagingBuffer.getAllItems().size}")
        }

        launchSideEffects(pagingStateManager.getState())

        // TODO: Handle prefetching

        data.nextOffset?.let {
            loader.value(it)
        }
    }

    private suspend fun onError(
        params: PagingSource.LoadParams<K>,
        error: PagingSource.LoadResult.Error<Id, K, V, E>
    ) {

        suspend fun passErrorThroughAndLaunchSideEffects() {
            pagingStateManager.update { prevState ->
                prevState.copy(status = StoreX.Paging.State.Status.Error(error.value))
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
                onLoading()
            },
            onError = { error ->
                onError(params, error)
            },
            onData = { data ->
                onData(params, data)
            }
        )

    private fun launchSideEffects(state: StoreX.Paging.State<Id, K, V, E>) {
        sideEffects.forEach { it(state) }
    }
}
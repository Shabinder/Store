package org.mobilenativefoundation.storex.paging

import org.mobilenativefoundation.storex.paging.impl.DefaultFetchingStrategy
import org.mobilenativefoundation.storex.paging.impl.RealAggregatingStrategy
import org.mobilenativefoundation.storex.paging.impl.RealJobCoordinator
import org.mobilenativefoundation.storex.paging.impl.RealLoader
import org.mobilenativefoundation.storex.paging.impl.RealMutablePagingBuffer
import org.mobilenativefoundation.storex.paging.impl.RealPager
import org.mobilenativefoundation.storex.paging.impl.RealPagingSourceController
import org.mobilenativefoundation.storex.paging.impl.RealPagingStateManager
import org.mobilenativefoundation.storex.paging.impl.RealQueueManager
import org.mobilenativefoundation.storex.paging.impl.RealRetriesManager
import org.mobilenativefoundation.storex.paging.impl.RetriesManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.plus
import org.mobilenativefoundation.store.store5.Store

class PagerBuilder<Id : Comparable<Id>, K : StoreXPaging.Key, V : Identifiable<Id>, E : StoreXPaging.Error>(
    private val dispatcher: CoroutineDispatcher,
) {

    private var initialState: StoreXPaging.State<Id, K, V, E>? = null
    private var mutablePagingBuffer: MutablePagingBuffer<Id, K, V, E>? = null

    private var pagingConfig: PagingConfig = PagingConfig()
    private val middleware: MutableList<Middleware<K>> = mutableListOf()
    private val sideEffects: MutableList<SideEffect<Id, K, V, E>> = mutableListOf()
    private val launchEffects: MutableList<LaunchEffect> = mutableListOf()
    private val operations: MutableList<Operation<Id, K, V>> = mutableListOf()

    private var errorHandlingStrategy: ErrorHandlingStrategy = ErrorHandlingStrategy.RetryLast(3)
    private var pagingBufferMaxSize: Int = 500

    private var fetchingStrategy: FetchingStrategy<Id, K, V, E> = DefaultFetchingStrategy()

    private lateinit var pagingSource: PagingSource<Id, K, V, E>

    private var loadingStrategy: PagingSource.LoadParams.Strategy =
        PagingSource.LoadParams.Strategy.CacheFirst(false)

    fun initialState(
        mutablePagingBuffer: MutablePagingBuffer<Id, K, V, E>,
        state: StoreXPaging.State<Id, K, V, E>
    ) = apply {
        this.initialState = state
        this.mutablePagingBuffer = mutablePagingBuffer
    }

    fun pagingConfig(config: PagingConfig) = apply { this.pagingConfig = config }
    fun pagingSource(
        pageStore: Store<K, PagingSource.LoadResult.Data<Id, K, V, E>>,
        throwableConverter: (Throwable) -> E,
        messageConverter: (String) -> E,
        builder: PagingSourceBuilder<Id, K, V, E>.() -> PagingSourceBuilder<Id, K, V, E>
    ) =
        apply {
            this.pagingSource = builder(
                PagingSourceBuilder(
                    dispatcher,
                    pageStore,
                    throwableConverter,
                    messageConverter
                )
            ).build()
        }

    fun pagingSource(
        pageStore: Store<K, PagingSource.LoadResult.Data<Id, K, V, E>>,
        throwableConverter: (Throwable) -> E,
        messageConverter: (String) -> E
    ) =
        apply {
            this.pagingSource = PagingSourceBuilder(
                dispatcher,
                pageStore,
                throwableConverter,
                messageConverter
            ).build()
        }

    fun pagingSource(pagingSource: PagingSource<Id, K, V, E>) =
        apply { this.pagingSource = pagingSource }

    fun middleware(builder: MutableList<Middleware<K>>.() -> MutableList<Middleware<K>>) =
        apply {
            this.middleware.addAll(builder(mutableListOf()))
        }

    fun sideEffects(builder: MutableList<SideEffect<Id, K, V, E>>.() -> MutableList<SideEffect<Id, K, V, E>>) =
        apply {
            this.sideEffects.addAll(builder(mutableListOf()))
        }

    fun launchEffects(builder: MutableList<LaunchEffect>.() -> MutableList<LaunchEffect>) =
        apply {
            this.launchEffects.addAll(builder(mutableListOf()))
        }

    fun operations(builder: MutableList<Operation<Id, K, V>>.() -> MutableList<Operation<Id, K, V>>) =
        apply {
            this.operations.addAll(builder(mutableListOf()))
        }

    fun errorHandlingStrategy(strategy: ErrorHandlingStrategy) =
        apply { this.errorHandlingStrategy = strategy }

    fun fetchingStrategy(strategy: FetchingStrategy<Id, K, V, E>) =
        apply { this.fetchingStrategy = strategy }

    fun pagingBufferMaxSize(maxSize: Int) = apply { this.pagingBufferMaxSize = maxSize }

    fun build(
        placeholderFactory: PlaceholderFactory<Id, V>,
        keyFactory: KeyFactory<Id, K>,
    ): Pager<Id, K, V, E> {
        val mutablePagingBuffer: MutablePagingBuffer<Id, K, V, E> =
            this.mutablePagingBuffer ?: RealMutablePagingBuffer(pagingBufferMaxSize)

        val initialState =
            this.initialState ?: StoreXPaging.State.Initial(null, null, mutablePagingBuffer)

        val pagingStateManager: PagingStateManager<Id, K, V, E> =
            RealPagingStateManager(initialState, mutablePagingBuffer)

        val aggregatingStrategy: AggregatingStrategy<Id, K, V, E> =
            RealAggregatingStrategy(operations, pagingConfig)


        val scope = CoroutineScope(dispatcher) + Job()

        val jobCoordinator = RealJobCoordinator(scope)

        val retriesManager: RetriesManager<K> = RealRetriesManager()

        val pagingSourceController: org.mobilenativefoundation.storex.paging.PagingSourceController<K> = RealPagingSourceController(
            sideEffects = sideEffects,
            pagingConfig = pagingConfig,
            jobCoordinator = jobCoordinator,
            pagingSource = pagingSource,
            pagingStateManager = pagingStateManager,
            retriesManager = retriesManager
        )

        val queueManager: QueueManager<K> = RealQueueManager(
            dispatcher = dispatcher,
            pagingConfig = pagingConfig,
            fetchingStrategy = fetchingStrategy,
            mutablePagingBuffer = mutablePagingBuffer,
            pagingSourceController = pagingSourceController,
            pagingStateProvider = pagingStateManager,
            placeholderFactory = placeholderFactory
        )

        val loader: Loader<Id, K, V, E> = RealLoader(
            middleware = middleware,
            queueManager = queueManager,
            strategy = loadingStrategy,
            keyFactory = keyFactory
        )

        return RealPager(
            dispatcher = dispatcher,
            pagingStateProvider = pagingStateManager,
            loader = loader,
            launchEffects = launchEffects,
            aggregatingStrategy = aggregatingStrategy
        )
    }
}
package org.mobilenativefoundation.storex.paging

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.storex.paging.Timeline.PLACEHOLDER_TAG
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class RealPagerTest {
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var server: Timeline.Backend.Server

    private lateinit var feedStore: Store<K, SPV>

    @BeforeTest
    fun setup() {
        server = Timeline.Backend.Server()
        feedStore = Timeline.StoreFactory.Feed.create(server.feedService)
    }


    private fun StandardTestPager(
        prefetchDistance: Int = 50,
        pageSize: Int = 10,
    ): Pager<Id, K, V, E> {
        val throwableConverter = { throwable: Throwable ->
            Timeline.Error.Exception(
                throwable,
                StoreX.Paging.DataSource.NETWORK
            )
        }

        val messageConverter = { message: String ->
            Timeline.Error.Exception(Throwable(message), StoreX.Paging.DataSource.NETWORK)
        }

        val keyFactory = KeyFactory<Id, K> {
            Timeline.GetFeedRequest(it, pageSize)
        }

        val placeholderFactory = PlaceholderFactory {
            StoreX.Paging.Data.Item(Timeline.Post(PLACEHOLDER_TAG), origin = StoreX.Paging.DataSource.PLACEHOLDER)
        }

        return PagerBuilder<Id, K, V, E>(
            testDispatcher
        ).pagingConfig(
            PagingConfig(
                pageSize = pageSize, // TODO
                prefetchDistance = prefetchDistance,
                initialLoadSize = 300 // TODO
            )
        )
            .pagingSource(
                pageStore = feedStore,
                throwableConverter = throwableConverter,
                messageConverter = messageConverter
            )
            .defaultFetchingStrategy()
            .pagingOffsetCalculator { key ->
                (key.cursor.toInt() + pageSize).toString()
            }
            .build(
                placeholderFactory = placeholderFactory,
                keyFactory = keyFactory
            )
    }

    @Test
    fun flow_givenPageSize10AndPrefetchDistance50_whenInvokePager_shouldTransitionBetweenLoadingStates5x() =
        testScope.runTest {
            // Given
            val prefetchDistance = 50
            val pageSize = 10
            val pager = StandardTestPager(prefetchDistance = prefetchDistance, pageSize = pageSize)
            val requests = MutableStateFlow("1")

            // When
            pager.start(requests)

            // Then
            val expectedLoadingMoreTransitionCount = prefetchDistance / pageSize

            var actualLoadingMoreTransitionCount = 0
            var lastPage: StoreX.Paging.State<Id, K, V, E>? = null

            pager.flow.test {
                // Initial
                val initial = awaitItem()
                assertEquals(StoreX.Paging.State.Status.Initial, initial.status)

                // Loading
                val initialLoading = awaitItem()
                assertEquals(StoreX.Paging.State.Status.Loading, initialLoading.status)

                // Idle
                val initialIdle = awaitItem()
                assertEquals(StoreX.Paging.State.Status.Idle, initialIdle.status)

                while (actualLoadingMoreTransitionCount < expectedLoadingMoreTransitionCount) {

                    // Loading more
                    val loadingMore = awaitItem()
                    assertEquals(StoreX.Paging.State.Status.Loading, loadingMore.status)

                    // Idle
                    val idle = awaitItem()
                    assertEquals(StoreX.Paging.State.Status.Idle, idle.status)

                    lastPage = idle
                    actualLoadingMoreTransitionCount++

                    val expectedItemCount = pageSize + (actualLoadingMoreTransitionCount * pageSize)
                    val actualItemCount = idle.pagingBuffer.getAllItems().size
                    assertEquals(expectedItemCount, actualItemCount)
                }

                advanceUntilIdle()
                expectNoEvents()

                val expectedItemCount = pageSize + (expectedLoadingMoreTransitionCount * pageSize)
                val actualItemCount = lastPage?.pagingBuffer?.getAllItems()?.size
                assertEquals(expectedItemCount, actualItemCount)

                val expectedItemIds = List(expectedItemCount) { "${it + 1}" }
                val actualItemIds = lastPage?.pagingBuffer?.getAllItems()?.map { it.value.id }
                assertEquals(expectedItemIds, actualItemIds)
            }
        }

    @Test
    fun pagingItems_givenPageSize10AndPrefetchDistance50_whenInvokePager_shouldEmitPagingItems6x() =
        testScope.runTest {
            // Given
            val prefetchDistance = 50
            val pageSize = 10
            val initialId = "1"
            val pager = StandardTestPager(prefetchDistance = prefetchDistance, pageSize = pageSize)
            val requests = MutableStateFlow(initialId)

            // When
            pager.start(requests)

            // Then
            var lastPagingItems: StoreX.Paging.AggregatedItems<Id, V>? = null
            var itemCount = 0

            pager.pagingItems.test {

                // Empty list
                val emptyList = awaitItem()
                assertIs<StoreX.Paging.AggregatedItems<Id, V>>(emptyList)
                lastPagingItems = emptyList

                // Placeholders
                val placeholders = awaitItem()
                assertEquals(true, placeholders.value.all { it.origin == StoreX.Paging.DataSource.PLACEHOLDER })

                while (itemCount < (prefetchDistance + pageSize) || lastPagingItems!!.value.any { it.origin == StoreX.Paging.DataSource.PLACEHOLDER }) {
                    // Items
                    val items = awaitItem()
                    assertIs<StoreX.Paging.AggregatedItems<Id, V>>(items)

                    lastPagingItems = items
                    itemCount = items.value.size
                }

                val expectedItemCount = pageSize + prefetchDistance
                val actualItemCount = lastPagingItems?.value?.size
                assertEquals(expectedItemCount, actualItemCount)
                assertEquals(true, lastPagingItems?.value?.none { it.origin == StoreX.Paging.DataSource.PLACEHOLDER })

                val expectedIds = List(expectedItemCount) { "${it + 1}" }
                assertEquals(expectedIds, lastPagingItems?.value?.map { it.value.id })

                advanceUntilIdle()
                expectNoEvents()
            }
        }
}


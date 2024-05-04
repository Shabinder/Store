package org.mobilenativefoundation.storex.paging

import app.cash.turbine.test
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.storex.paging.Timeline.PLACEHOLDER_TAG
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

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

    private fun StandardTestPager(): Pager<Id, K, V, E> {
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
            Timeline.GetFeedRequest(it, 10)
        }

        val placeholderFactory = PlaceholderFactory {
            StoreX.Paging.Data.Item(Timeline.Post(PLACEHOLDER_TAG))
        }

        return PagerBuilder<Id, K, V, E>(
            testDispatcher
        ).pagingSource(
            pageStore = feedStore,
            throwableConverter = throwableConverter,
            messageConverter = messageConverter
        ).build(
            placeholderFactory = placeholderFactory,
            keyFactory = keyFactory
        )
    }

    @Test
    fun flow_whenSuccessfulRequest3x_shouldEmitPagingStateTransition3x() = testScope.runTest {
        val pager = StandardTestPager()
        val requests = MutableStateFlow(
            "1"
        )

        pager(requests)

        pager.flow.test {

            // Initial
            val initial = awaitItem()
            assertEquals(StoreX.Paging.State.Status.Initial, initial.status)

            // Loading
            val loading = awaitItem()
            assertEquals(StoreX.Paging.State.Status.Loading, loading.status)

            // Idle
            val idle = awaitItem()
            assertEquals(StoreX.Paging.State.Status.Idle, idle.status)

            // Loading more
            val loadingMore = awaitItem()
            assertEquals(StoreX.Paging.State.Status.Loading, loadingMore.status)

            // Idle
            val idle2 = awaitItem()
            assertEquals(StoreX.Paging.State.Status.Idle, idle2.status)

            // Loading more
            val loadingMore2 = awaitItem()
            assertEquals(StoreX.Paging.State.Status.Loading, loadingMore2.status)

            // Idle
            val idle3 = awaitItem()
            assertEquals(StoreX.Paging.State.Status.Idle, idle3.status)

            expectNoEvents()
        }
    }

    @Test
    fun pagingItems_whenSuccessfulRequest3x_shouldEmitPagingItems3x() = testScope.runTest {
        val pager = StandardTestPager()
        val requests = MutableStateFlow("1")

        pager.pagingItems.test {

            pager(requests)

            // Items
            val items1 = awaitItem()
            assertIs<StoreX.Paging.Items<Id, V>>(items1)

            // Items + Placeholders
            val itemsAndPlaceholders = awaitItem()
            assertIs<StoreX.Paging.Items<Id, V>>(itemsAndPlaceholders)


            // Items
            val items2 = awaitItem()
            assertIs<StoreX.Paging.Items<Id, V>>(items2)

            // Items + Placeholders
            val itemsAndPlaceholders2 = awaitItem()
            assertIs<StoreX.Paging.Items<Id, V>>(itemsAndPlaceholders2)


            // Items
            val items3 = awaitItem()
            assertIs<StoreX.Paging.Items<Id, V>>(items3)

            expectNoEvents()

        }
    }
}


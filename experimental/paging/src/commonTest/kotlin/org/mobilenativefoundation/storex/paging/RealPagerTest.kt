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
                StoreXPaging.DataSource.NETWORK
            )
        }

        val messageConverter = { message: String ->
            Timeline.Error.Exception(Throwable(message), StoreXPaging.DataSource.NETWORK)
        }

        val keyFactory = KeyFactory<Id, K> {
            Timeline.GetFeedRequest(it, 10)
        }

        val placeholderFactory = PlaceholderFactory {
            StoreXPaging.Data.Item(Timeline.Post(PLACEHOLDER_TAG))
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

        pager.flow.test {

            pager(requests)

            // Initial
            val initial = awaitItem()
            assertIs<StoreXPaging.State.Initial<Id, K, V, E>>(initial)

            // Loading
            val loading = awaitItem()
            assertIs<StoreXPaging.State.Loading<Id, K, V, E>>(loading)

            // Idle
            val idle = awaitItem()
            assertIs<StoreXPaging.State.Idle<Id, K, V, E>>(idle)

            requests.emit(
                "11"
            )

            // Loading more
            val loadingMore = awaitItem()
            assertIs<StoreXPaging.State.LoadingMore<Id, K, V, E>>(loadingMore)

            // Idle
            val idle2 = awaitItem()
            assertIs<StoreXPaging.State.Idle<Id, K, V, E>>(idle2)

            requests.emit(
                "21"
            )

            // Loading more
            val loadingMore2 = awaitItem()
            assertIs<StoreXPaging.State.LoadingMore<Id, K, V, E>>(loadingMore2)


            // Idle
            val idle3 = awaitItem()
            assertIs<StoreXPaging.State.Idle<Id, K, V, E>>(idle3)

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
            assertIs<StoreXPaging.Items<Id, V>>(items1)

            requests.emit("11")

            // Items + Placeholders
            val itemsAndPlaceholders = awaitItem()
            assertIs<StoreXPaging.Items<Id, V>>(itemsAndPlaceholders)


            // Items
            val items2 = awaitItem()
            assertIs<StoreXPaging.Items<Id, V>>(items2)

            requests.emit("21")

            // Items + Placeholders
            val itemsAndPlaceholders2 = awaitItem()
            assertIs<StoreXPaging.Items<Id, V>>(itemsAndPlaceholders2)


            // Items
            val items3 = awaitItem()
            assertIs<StoreXPaging.Items<Id, V>>(items3)

            expectNoEvents()

        }
    }
}


package org.mobilenativefoundation.storex.paging

import kotlinx.datetime.Clock


object StoreX {


    object Paging {
        sealed interface Data<Id : Comparable<Id>, out K : Any, out V : Identifiable<Id>> {


            data class Item<Id : Comparable<Id>, out V : Identifiable<Id>>(
                val value: V
            ) : Data<Id, Nothing, V>

            data class Page<Id : Comparable<Id>, out K : Any, out V : Identifiable<Id>>(
                val items: List<Id>,
                val key: K,
                val nextKey: K?,
                val itemsBefore: Int?,
                val itemsAfter: Int?,
                val origin: DataSource,
                val extras: Map<String, Any> = mapOf()
            ) : Data<Id, K, V>
        }

        enum class DataSource {
            MEMORY_CACHE,
            SOURCE_OF_TRUTH,
            NETWORK,
            PLACEHOLDER
        }

        sealed interface State<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any> {
            val anchorPosition: K?
            val prefetchPosition: K?
            val pagingBuffer: PagingBuffer<Id, K, V, E>

            data class Initial<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
                override val prefetchPosition: K?,
                override val anchorPosition: K?,
                override val pagingBuffer: PagingBuffer<Id, K, V, E>
            ) : State<Id, K, V, E>

            data class Loading<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
                override val prefetchPosition: K?,
                override val anchorPosition: K?,
                override val pagingBuffer: PagingBuffer<Id, K, V, E>
            ) : State<Id, K, V, E>

            data class Error<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
                val value: E,
                override val prefetchPosition: K?,
                override val anchorPosition: K?,
                override val pagingBuffer: PagingBuffer<Id, K, V, E>
            ) : State<Id, K, V, E>

            sealed interface Data<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any> :
                State<Id, K, V, E>

            data class Idle<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
                override val pagingBuffer: PagingBuffer<Id, K, V, E>,
                override val anchorPosition: K?,
                override val prefetchPosition: K?,
                private val created: Long = Clock.System.now().epochSeconds,
            ) : Data<Id, K, V, E>

            data class LoadingMore<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
                override val pagingBuffer: PagingBuffer<Id, K, V, E>,
                override val anchorPosition: K?,
                override val prefetchPosition: K?,
            ) : Data<Id, K, V, E>


            data class ErrorLoadingMore<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
                override val pagingBuffer: PagingBuffer<Id, K, V, E>,
                val error: E,
                override val anchorPosition: K?,
                override val prefetchPosition: K?,
            ) : Data<Id, K, V, E>
        }

        data class Items<Id : Comparable<Id>, out V : Identifiable<Id>>(
            val value: List<Data.Item<Id, V>>
        )

    }

}
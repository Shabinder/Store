package org.mobilenativefoundation.storex.paging


object StoreX {


    object Paging {
        sealed interface Data<Id : Comparable<Id>, out K : Any, out V : Identifiable<Id>> {


            data class Item<Id : Comparable<Id>, out V : Identifiable<Id>>(
                val value: V,
                val origin: DataSource
            ) : Data<Id, Nothing, V>

            data class Page<Id : Comparable<Id>, out K : Any, out V : Identifiable<Id>>(
                val items: List<Id>,
                val key: K,
                val next: Id?,
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


        data class State<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any>(
            val pagingBuffer: PagingBuffer<Id, K, V, E>,
            val status: Status<E> = Status.Initial,
        ) {

            sealed interface Status<out E : Any> {
                data object Initial : Status<Nothing>
                data object Loading : Status<Nothing>
                data class Error<E : Any>(val error: E) : Status<E>
                data object Idle : Status<Nothing>
            }
        }

        data class AggregatedItems<Id : Comparable<Id>, out V : Identifiable<Id>>(
            val value: List<Data.Item<Id, V>> = emptyList()
        )

    }

}
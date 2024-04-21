package org.mobilenativefoundation.market.repository


fun interface Read<K : Any, D : Any, E : Any> {
    suspend fun read(request: Request<K>): Response<D, E>

    data class Request<K : Any>(
        val key: K,
        val strategy: Strategy
    )

    sealed interface Response<out D : Any, out E : Any> {
        data class Success<D : Any>(val data: D, val origin: DataSource) : Response<D, Nothing>
        data class Error<E : Any>(val error: E) : Response<Nothing, E>
    }

    data class Strategy(
        val order: List<DataSource>,
        val conflictResolutionStrategy: ConflictResolutionStrategy
    ) {
        companion object {
            val CACHE_FIRST = Strategy(
                order = listOf(
                    DataSource.MEMORY_CACHE,
                    DataSource.SOURCE_OF_TRUTH,
                    DataSource.NETWORK
                ),
                conflictResolutionStrategy = ConflictResolutionStrategy.DEFER
            )
        }
    }
}



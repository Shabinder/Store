package org.mobilenativefoundation.market.repository

fun interface Delete<K : Any, D : Any, E : Any> {
    suspend fun delete(request: Request<K, D>): Response<E>

    data class Request<K : Any, D : Any>(
        val key: K,
        val strategy: Strategy
    )

    data class Strategy(
        val caches: Boolean,
        val network: Boolean
    )

    sealed interface Response<out E : Any> {
        data object Success : Response<Nothing>

        data class Error<E : Any>(
            val error: E
        ) : Response<E>
    }

}
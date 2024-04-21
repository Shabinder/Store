package org.mobilenativefoundation.market.repository

fun interface Write<K : Any, D : Any, E : Any> {
    fun write(request: Request<K, D>): Response<D, E>

    data class Request<K : Any, D : Any>(
        val key: K,
        val data: D,
        val strategy: Strategy
    )

    data class Strategy(
        val caches: Boolean,
        val network: Boolean
    )

    sealed interface Response<D : Any, E : Any> {
        data class Success<D : Any>(
            val data: D
        ) : Response<D, Nothing>

        data class Error<E : Any>(
            val error: E
        ) : Response<Nothing, E>
    }

}
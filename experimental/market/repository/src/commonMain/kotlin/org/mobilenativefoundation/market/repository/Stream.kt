package org.mobilenativefoundation.market.repository

import kotlinx.coroutines.flow.Flow

fun interface Stream<K : Any, D : Any, E : Any> {
    fun stream(request: Request<K>): Flow<Response<D, E>>

    data class Request<K : Any>(
        val key: K,
        val strategy: Strategy
    )

    sealed interface Response<out D : Any, out E : Any> {
        data class Success<D : Any>(val data: D) : Response<D, Nothing>
        data class Error<E : Any>(val error: E) : Response<Nothing, E>
        data object Loading : Response<Nothing, Nothing>
    }

    sealed interface Strategy
}
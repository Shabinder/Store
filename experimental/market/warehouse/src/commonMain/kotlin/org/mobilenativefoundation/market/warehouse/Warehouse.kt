package org.mobilenativefoundation.market.warehouse

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface Warehouse<S : Warehouse.State, A : Warehouse.Action> {
    val state: StateFlow<S>
    fun dispatch(action: A)
    fun dispatch(worker: Worker<S, A>)
    fun <D : Any> subscribe(selector: Selector<S, D>): Flow<D>

    interface Action
    interface State
    fun interface Worker<S : State, A : Action> {
        fun work(getState: () -> S, dispatch: (action: A) -> Unit)
    }

    fun interface Selector<S : State, D : Any> {
        fun select(state: S): D
    }

    fun interface Middleware<A: Action> {
        fun apply(action: A, next: (action: A) -> Unit)
    }
}

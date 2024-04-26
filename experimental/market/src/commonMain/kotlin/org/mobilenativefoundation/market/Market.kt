package org.mobilenativefoundation.market

import kotlinx.coroutines.flow.StateFlow

interface Market<S: Market.State> {

    val state: StateFlow<S>

    interface State
    interface Action
    interface Middleware

    fun interface Reducer<S : State, A : Action> {
        fun reduce(state: S, action: A): S
    }

    interface Dispatcher<A : Action> {
        fun dispatch(action: A)
    }

    fun interface Selector<S: State, R: Any> {
        fun select(state: S): R
    }
}
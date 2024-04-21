package org.mobilenativefoundation.market

interface Market {
    interface State
    interface Action
    interface Middleware

    fun interface Reducer<S : State, A : Action> {
        fun reduce(state: S, action: A): S
    }
}
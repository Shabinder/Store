package org.mobilenativefoundation.market


inline fun <reified S : Market.State, reified A : Market.Action> combineReducers(vararg reducers: Market.Reducer<S, A>): Market.Reducer<S, A> {
    return Market.Reducer { state, action ->
        reducers.fold(state) { currentState, reducer ->
            reducer.reduce(currentState, action)
        }
    }
}


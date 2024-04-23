package org.mobilenativefoundation.market

interface MutableMarket<S : Market.State> : Market<S> {
    fun updateState(nextState: S)
}
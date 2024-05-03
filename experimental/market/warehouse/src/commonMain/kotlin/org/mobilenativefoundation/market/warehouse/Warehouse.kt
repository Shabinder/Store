package org.mobilenativefoundation.market.warehouse

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.mobilenativefoundation.market.Market
import org.mobilenativefoundation.market.warehouse.impl.RealWarehouse


interface Warehouse<S : Warehouse.State, A : Warehouse.Action> {

    val state: StateFlow<S>
    fun dispatch(action: A)
    fun <D : Any> subscribe(selector: Selector<S, D>): Flow<D>

    interface Action {
        interface Async : Action
    }

    interface State

    fun interface Selector<S : State, D : Any> {
        fun select(state: S): D
    }


    companion object {
        fun <S : State, A : Action, MS : Market.State, M : Market<MS>> from(
            coroutineDispatcher: CoroutineDispatcher,
            market: M,
            selector: Market.Selector<MS, S>,
            actionHandler: (A, MS) -> Unit
        ): Warehouse<S, A> {
            return RealWarehouse(coroutineDispatcher, market, selector, actionHandler)
        }
    }
}

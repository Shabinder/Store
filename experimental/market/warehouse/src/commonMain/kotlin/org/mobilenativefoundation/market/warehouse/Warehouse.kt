package org.mobilenativefoundation.market.warehouse

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import org.mobilenativefoundation.market.Market


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
}

class RealWarehouse<S : Warehouse.State, A : Warehouse.Action, MS : Market.State, M : Market<MS>>(
    coroutineDispatcher: CoroutineDispatcher,
    private val market: M,
    private val extractor: (MS) -> S,
    private val actionHandler: (A, MS) -> Unit
) : Warehouse<S, A> {

    private val coroutineScope = CoroutineScope(coroutineDispatcher)

    override val state: StateFlow<S> = market.state.map {
        extractor(it)
    }.stateIn(coroutineScope, SharingStarted.Eagerly, extractor(market.state.value))


    override fun <D : Any> subscribe(selector: Warehouse.Selector<S, D>): Flow<D> =
        state.map { selector.select(it) }

    override fun dispatch(action: A) {
        actionHandler(action, market.state.value)
    }
}
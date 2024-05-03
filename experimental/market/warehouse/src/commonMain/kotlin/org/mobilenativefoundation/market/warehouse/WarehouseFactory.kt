package org.mobilenativefoundation.market.warehouse

import kotlinx.coroutines.CoroutineDispatcher
import org.mobilenativefoundation.market.Market

class WarehouseFactory<S : Warehouse.State, A : Warehouse.Action, MS : Market.State, M : Market<MS>>(
    private val market: M,
) {
    fun create(
        actionHandler: (A, MS) -> Unit,
        selector: Market.Selector<MS, S>,
        coroutineDispatcher: CoroutineDispatcher
    ): Warehouse<S, A> {
        return Warehouse.from(
            coroutineDispatcher, market, selector, actionHandler
        )
    }
}
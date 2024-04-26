package org.mobilenativefoundation.sample.octonaut.xplat.common.market

import kotlinx.coroutines.CoroutineDispatcher
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.market.Market
import org.mobilenativefoundation.market.warehouse.RealWarehouse
import org.mobilenativefoundation.market.warehouse.Warehouse


interface WarehouseBuilder<S : Warehouse.State, A : Warehouse.Action> {
    fun <P : Any> memoizedSelector(
        getParams: (OctonautMarketState) -> P,
        areParamsEqual: (P, P) -> Boolean = { p1, p2 -> p1 == p2 },
        selectState: (OctonautMarketState) -> S,
    ): WarehouseBuilder<S, A>

    fun selector(
        selector: Market.Selector<OctonautMarketState, S>
    ): WarehouseBuilder<S, A>

    fun actionHandler(actionHandler: (A, OctonautMarketState) -> Unit): WarehouseBuilder<S, A>
    fun build(): Warehouse<S, A>
}

interface WarehouseBuilderFactory {
    fun <S : Warehouse.State, A : Warehouse.Action> create(): WarehouseBuilder<S, A>
}


@Inject
class RealWarehouseBuilderFactory(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val market: OctonautMarket
) : WarehouseBuilderFactory {
    override fun <S : Warehouse.State, A : Warehouse.Action> create(): WarehouseBuilder<S, A> {
        return object : WarehouseBuilder<S, A> {

            private lateinit var actionHandler: (A, OctonautMarketState) -> Unit
            private lateinit var selector: Market.Selector<OctonautMarketState, S>


            override fun actionHandler(actionHandler: (A, OctonautMarketState) -> Unit) = apply {
                this.actionHandler = actionHandler
            }


            override fun selector(selector: Market.Selector<OctonautMarketState, S>): WarehouseBuilder<S, A> = apply {
                this.selector = selector
            }

            override fun <P : Any> memoizedSelector(
                getParams: (OctonautMarketState) -> P,
                areParamsEqual: (P, P) -> Boolean,
                selectState: (OctonautMarketState) -> S
            ): WarehouseBuilder<S, A> = apply {
                this.selector = memoize(
                    selectState = selectState,
                    getParams = getParams,
                    areParamsEqual = areParamsEqual
                )
            }

            override fun build(): Warehouse<S, A> {
                return RealWarehouse(
                    coroutineDispatcher,
                    market,
                    selector,
                    actionHandler
                )
            }

        }
    }
}

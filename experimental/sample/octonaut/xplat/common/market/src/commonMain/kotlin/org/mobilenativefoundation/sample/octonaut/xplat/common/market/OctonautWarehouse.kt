package org.mobilenativefoundation.sample.octonaut.xplat.common.market

import kotlinx.coroutines.CoroutineDispatcher
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.market.warehouse.RealWarehouse
import org.mobilenativefoundation.market.warehouse.Warehouse


interface WarehouseBuilder<S : Warehouse.State, A : Warehouse.Action> {

    fun extractor(extractor: (OctonautMarketState) -> S): WarehouseBuilder<S, A>
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

            private lateinit var extractor: (OctonautMarketState) -> S
            private lateinit var actionHandler: (A, OctonautMarketState) -> Unit

            override fun extractor(extractor: (OctonautMarketState) -> S) = apply {
                this.extractor = extractor
            }

            override fun actionHandler(actionHandler: (A, OctonautMarketState) -> Unit) = apply {
                this.actionHandler = actionHandler
            }

            override fun build(): Warehouse<S, A> {
                return RealWarehouse(
                    coroutineDispatcher,
                    market,
                    extractor,
                    actionHandler
                )
            }

        }
    }
}

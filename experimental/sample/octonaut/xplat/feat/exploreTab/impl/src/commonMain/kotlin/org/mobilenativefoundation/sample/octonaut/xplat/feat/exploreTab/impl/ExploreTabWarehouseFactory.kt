package org.mobilenativefoundation.sample.octonaut.xplat.feat.exploreTab.impl

import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.WarehouseBuilderFactory

@Inject
class ExploreTabWarehouseFactory(
    private val warehouseBuilderFactory: WarehouseBuilderFactory
) {
    fun create(): ExploreTabWarehouse {
        val warehouseBuilder = warehouseBuilderFactory.create<
                ExploreTabWarehouseState, ExploreTabWarehouseAction>()

        return warehouseBuilder.extractor { marketState ->
            ExploreTabWarehouseState(
                user = marketState.currentUser?.user,
                searchResults = emptyList() // TODO
            )
        }.actionHandler { action, marketState ->
            when (action) {
                is ExploreTabWarehouseAction.Search -> {
                    // TODO
                }
            }
        }.build()
    }
}


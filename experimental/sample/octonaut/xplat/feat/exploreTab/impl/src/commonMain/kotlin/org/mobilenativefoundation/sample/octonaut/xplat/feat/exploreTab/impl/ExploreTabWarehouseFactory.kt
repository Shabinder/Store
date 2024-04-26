package org.mobilenativefoundation.sample.octonaut.xplat.feat.exploreTab.impl

import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.WarehouseBuilderFactory

@Inject
class ExploreTabWarehouseFactory(
    warehouseBuilderFactory: WarehouseBuilderFactory
) {

    private val warehouseBuilder = warehouseBuilderFactory.create<ExploreTabWarehouseState, ExploreTabWarehouseAction>()

    fun create(): ExploreTabWarehouse =
        warehouseBuilder
            .memoizedSelector(getParams = { _ -> }) { state ->
                ExploreTabWarehouseState(
                    state.currentUser.user,
                    emptyList()
                )
            }
            .actionHandler { action, _ ->
                when (action) {
                    is ExploreTabWarehouseAction.Search -> {
                        // TODO
                    }
                }
            }.build()
}

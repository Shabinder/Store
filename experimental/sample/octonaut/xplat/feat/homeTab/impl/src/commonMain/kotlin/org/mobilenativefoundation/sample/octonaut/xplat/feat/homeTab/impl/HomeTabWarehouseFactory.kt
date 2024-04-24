package org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.impl

import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.WarehouseBuilderFactory
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.UserSupplier
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.GetUserQuery


@Inject
class HomeTabWarehouseFactory(
    private val userSupplier: UserSupplier,
    private val warehouseBuilderFactory: WarehouseBuilderFactory
) {
    fun create(): HomeTabWarehouse {
        val warehouseBuilder = warehouseBuilderFactory
            .create<HomeTabWarehouseState, HomeTabWarehouseAction>()

        return warehouseBuilder
            .extractor { marketState ->
                HomeTabWarehouseState(marketState.user)
            }
            .actionHandler { action, marketState ->
                when (action) {
                    HomeTabWarehouseAction.Refresh -> {
                        // Refresh user
                        val currentUser = marketState.user!!
                        userSupplier.supply(GetUserQuery(currentUser.login))

                        // Refresh repositories
                        // TODO
                    }
                }
            }
            .build()
    }
}
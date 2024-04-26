package org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.impl

import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.WarehouseBuilderFactory
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.UserSupplier
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.GetUserQuery


@Inject
class HomeTabWarehouseFactory(
    private val userSupplier: UserSupplier,
    warehouseBuilderFactory: WarehouseBuilderFactory
) {
    private val warehouseBuilder = warehouseBuilderFactory.create<HomeTabWarehouseState, HomeTabWarehouseAction>()

    fun create(): HomeTabWarehouse =
        warehouseBuilder
            .memoizedSelector(getParams = { _ -> }) { state ->
                HomeTabWarehouseState(
                    state.currentUser.user,
                    state.feed
                )
            }
            .actionHandler { action, marketState ->
                when (action) {
                    HomeTabWarehouseAction.Refresh -> {
                        // Refresh user
                        val currentUser = marketState.currentUser.user!!
                        userSupplier.supply(GetUserQuery(currentUser.login))

                        // Refresh repositories
                        // TODO
                    }
                }
            }
            .build()
}
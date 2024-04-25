package org.mobilenativefoundation.sample.octonaut.xplat.feat.userProfile.impl

import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.WarehouseBuilderFactory
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.UserSupplier
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.GetUserQuery

@Inject
class UserProfileScreenWarehouseFactory(
    private val warehouseBuilderFactory: WarehouseBuilderFactory,
    private val userSupplier: UserSupplier
) {
    fun create(): UserProfileScreenWarehouse {
        val warehouseBuilder =
            warehouseBuilderFactory.create<UserProfileScreenWarehouseState, UserProfileScreenWarehouseAction>()

        return warehouseBuilder.extractor { marketState ->
            UserProfileScreenWarehouseState(marketState.notifications)
        }
            .actionHandler { action, marketState ->
                when (action) {
                    is UserProfileScreenWarehouseAction.LoadUser -> userSupplier.supply(GetUserQuery(action.login))
                }
            }
            .build()
    }
}
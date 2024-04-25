package org.mobilenativefoundation.sample.octonaut.xplat.feat.userProfile.impl

import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.WarehouseBuilderFactory
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.UserSupplier
import org.mobilenativefoundation.sample.octonaut.xplat.feat.userProfile.api.UserProfileScreen
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.GetUserQuery

@Inject
class UserProfileScreenWarehouseFactory(
    @Assisted private val login: String,
    private val warehouseBuilderFactory: WarehouseBuilderFactory,
    private val userSupplier: UserSupplier,
) {
    fun create(): UserProfileScreenWarehouse {
        val warehouseBuilder =
            warehouseBuilderFactory.create<UserProfileScreenWarehouseState, UserProfileScreenWarehouseAction>()

        return warehouseBuilder.extractor { marketState ->
            val userId = marketState.users.loginToId[login]
            val user = marketState.users.byId[userId]
            UserProfileScreenWarehouseState(user)
        }
            .actionHandler { action, marketState ->
                when (action) {
                    is UserProfileScreenWarehouseAction.LoadUser -> userSupplier.supply(GetUserQuery(action.login))
                }
            }
            .build()
    }
}
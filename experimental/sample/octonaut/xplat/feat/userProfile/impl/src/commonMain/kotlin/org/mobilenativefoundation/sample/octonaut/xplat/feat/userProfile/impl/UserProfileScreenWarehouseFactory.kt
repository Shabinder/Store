package org.mobilenativefoundation.sample.octonaut.xplat.feat.userProfile.impl

import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.WarehouseBuilderFactory
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.UserSupplier
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.GetUserQuery

@Inject
class UserProfileScreenWarehouseFactory(
    @Assisted private val login: String,
    warehouseBuilderFactory: WarehouseBuilderFactory,
    private val userSupplier: UserSupplier,
) {
    private val warehouseBuilder =
        warehouseBuilderFactory.create<UserProfileScreenWarehouseState, UserProfileScreenWarehouseAction>()

    fun create(): UserProfileScreenWarehouse =
        warehouseBuilder
            .memoizedSelector(getParams = { state ->
                state.users.loginToId[login]?.let {
                    state.users.byId[it]
                } ?: Unit
            }) { state ->
                val userId = state.users.loginToId[login]
                val user = state.users.byId[userId]
                UserProfileScreenWarehouseState(user)
            }
            .actionHandler { action, _ ->
                when (action) {
                    is UserProfileScreenWarehouseAction.LoadUser -> userSupplier.supply(GetUserQuery(action.login))
                }
            }
            .build()
}
package org.mobilenativefoundation.sample.octonaut.xplat.feat.userProfile.impl

import org.mobilenativefoundation.market.warehouse.Warehouse

data class UserProfileScreenWarehouseState(
    val user: Any
) : Warehouse.State

sealed interface UserProfileScreenWarehouseAction : Warehouse.Action {
    data class LoadUser(
        val login: String
    ) : UserProfileScreenWarehouseAction
}

typealias UserProfileScreenWarehouse = Warehouse<UserProfileScreenWarehouseState, UserProfileScreenWarehouseAction>
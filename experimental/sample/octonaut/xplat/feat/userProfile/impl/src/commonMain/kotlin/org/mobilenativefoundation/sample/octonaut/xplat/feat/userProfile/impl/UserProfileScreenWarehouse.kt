package org.mobilenativefoundation.sample.octonaut.xplat.feat.userProfile.impl

import org.mobilenativefoundation.market.warehouse.Warehouse
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.User

data class UserProfileScreenWarehouseState(
    val user: User?
) : Warehouse.State

sealed interface UserProfileScreenWarehouseAction : Warehouse.Action {
    data class LoadUser(
        val login: String
    ) : UserProfileScreenWarehouseAction
}

typealias UserProfileScreenWarehouse = Warehouse<UserProfileScreenWarehouseState, UserProfileScreenWarehouseAction>
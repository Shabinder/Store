package org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.impl

import org.mobilenativefoundation.market.warehouse.Warehouse
import org.mobilenativefoundation.sample.octonaut.xplat.domain.feed.api.FeedState
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.User

data class HomeTabWarehouseState(
    val user: User? = null,
    val feed: FeedState? = null
) : Warehouse.State

sealed interface HomeTabWarehouseAction : Warehouse.Action {
    data object Refresh : HomeTabWarehouseAction
}

typealias HomeTabWarehouse = Warehouse<HomeTabWarehouseState, HomeTabWarehouseAction>


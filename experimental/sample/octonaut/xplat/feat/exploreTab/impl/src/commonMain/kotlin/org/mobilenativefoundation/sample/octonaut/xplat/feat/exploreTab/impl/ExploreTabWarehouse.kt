package org.mobilenativefoundation.sample.octonaut.xplat.feat.exploreTab.impl

import org.mobilenativefoundation.market.warehouse.Warehouse
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.User

data class ExploreTabWarehouseState(
    val user: User? = null,
    val searchResults: List<Any>,
) : Warehouse.State

sealed interface ExploreTabWarehouseAction : Warehouse.Action {
    data class Search(val searchInput: String) : ExploreTabWarehouseAction
}

typealias ExploreTabWarehouse = Warehouse<ExploreTabWarehouseState, ExploreTabWarehouseAction>
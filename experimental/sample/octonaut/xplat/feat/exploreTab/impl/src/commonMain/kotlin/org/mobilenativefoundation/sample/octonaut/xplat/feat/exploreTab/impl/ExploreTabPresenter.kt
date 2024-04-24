package org.mobilenativefoundation.sample.octonaut.xplat.feat.exploreTab.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.feat.exploreTab.api.ExploreTab

@Inject
class ExploreTabPresenter(private val warehouse: ExploreTabWarehouse) : ExploreTab.Presenter {

    private val searchInput = mutableStateOf<String?>(null)

    private fun on(event: ExploreTab.Event) {
        when (event) {
            is ExploreTab.Event.Search -> {
                warehouse.dispatch(ExploreTabWarehouseAction.Search(event.searchInput))
            }

            is ExploreTab.Event.UpdateSearchInput -> {
                searchInput.value = event.searchInput
            }
        }
    }

    @Composable
    override fun present(): ExploreTab.State {
        val warehouseState = warehouse.state.collectAsState()

        return warehouseState.value.user?.let { user ->
            ExploreTab.State.Loaded(user, searchInput.value, ::on)
        } ?: ExploreTab.State.Loading(::on)
    }
}
package org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.api.HomeTab

@Inject
class HomeTabPresenter(private val warehouse: HomeTabWarehouse) : HomeTab.Presenter {

    private fun on(event: HomeTab.Event) {
        when (event) {
            HomeTab.Event.Refresh -> warehouse.dispatch(HomeTabWarehouseAction.Refresh)
        }
    }

    @Composable
    override fun present(): HomeTab.State {
        val warehouseState = warehouse.state.collectAsState()

        return warehouseState.value.user?.let { user ->
            HomeTab.State.Loaded(user, ::on)
        } ?: HomeTab.State.Loading(::on)
    }

}
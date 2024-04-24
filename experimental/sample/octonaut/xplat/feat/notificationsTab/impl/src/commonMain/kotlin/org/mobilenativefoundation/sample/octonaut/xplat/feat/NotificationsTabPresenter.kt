package org.mobilenativefoundation.sample.octonaut.xplat.feat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.feat.notificationsTab.api.NotificationsTab

@Inject
class NotificationsTabPresenter(private val warehouse: NotificationsTabWarehouse) : NotificationsTab.Presenter {
    private fun on(event: NotificationsTab.Event) {
        when (event) {
            is NotificationsTab.Event.MarkDone -> warehouse.dispatch(
                NotificationsTabWarehouseAction.MarkDone(
                    event.notificationId
                )
            )
        }
    }

    @Composable
    override fun present(): NotificationsTab.State {
        val warehouseState = warehouse.state.collectAsState()

        return warehouseState.value.notifications.let {
            if (it.isEmpty()) {
                NotificationsTab.State.Loading
            } else {
                NotificationsTab.State.Loaded(it, ::on)
            }
        }
    }

}
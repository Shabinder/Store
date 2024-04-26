package org.mobilenativefoundation.sample.octonaut.xplat.feat

import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.WarehouseBuilderFactory
import org.mobilenativefoundation.sample.octonaut.xplat.domain.notifications.api.NotificationsSupplier

@Inject
class NotificationsTabWarehouseFactory(
    private val notificationsSupplier: NotificationsSupplier,
    warehouseBuilderFactory: WarehouseBuilderFactory
) {
    private val warehouseBuilder =
        warehouseBuilderFactory.create<NotificationsTabWarehouseState, NotificationsTabWarehouseAction>()

    fun create(): NotificationsTabWarehouse =
        warehouseBuilder
            .memoizedSelector(getParams = { _ -> }) { state ->
                NotificationsTabWarehouseState(state.notifications.allIds.mapNotNull { state.notifications.byId[it] })
            }
            .actionHandler { action, _ ->
                when (action) {
                    is NotificationsTabWarehouseAction.MarkDone -> {
                        // TODO
                    }
                }
            }
            .build()
}
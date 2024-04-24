package org.mobilenativefoundation.sample.octonaut.xplat.feat

import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.WarehouseBuilderFactory
import org.mobilenativefoundation.sample.octonaut.xplat.domain.notifications.api.NotificationsSupplier

@Inject
class NotificationsTabWarehouseFactory(
    private val notificationsSupplier: NotificationsSupplier,
    private val warehouseBuilderFactory: WarehouseBuilderFactory
) {
    fun create(): NotificationsTabWarehouse {
        val warehouseBuilder =
            warehouseBuilderFactory.create<NotificationsTabWarehouseState, NotificationsTabWarehouseAction>()

        return warehouseBuilder.extractor { marketState ->
            NotificationsTabWarehouseState(marketState.notifications)
        }
            .actionHandler { action, marketState ->
                when (action) {
                    is NotificationsTabWarehouseAction.MarkDone -> {
                        // TODO
                    }
                }
            }
            .build()
    }
}
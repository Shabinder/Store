package org.mobilenativefoundation.sample.octonaut.xplat.feat

import org.mobilenativefoundation.market.warehouse.Warehouse
import org.mobilenativefoundation.sample.octonaut.xplat.domain.notifications.api.Notification

data class NotificationsTabWarehouseState(
    val notifications: List<Notification>
) : Warehouse.State

sealed interface NotificationsTabWarehouseAction : Warehouse.Action {
    data class MarkDone(
        val notificationId: String
    ) : NotificationsTabWarehouseAction
}

typealias NotificationsTabWarehouse = Warehouse<NotificationsTabWarehouseState, NotificationsTabWarehouseAction>
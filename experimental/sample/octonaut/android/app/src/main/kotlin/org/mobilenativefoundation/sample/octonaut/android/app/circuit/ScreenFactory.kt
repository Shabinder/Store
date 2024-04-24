package org.mobilenativefoundation.sample.octonaut.android.app.circuit

import org.mobilenativefoundation.sample.octonaut.xplat.feat.exploreTab.api.ExploreTab
import org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.api.HomeTab
import org.mobilenativefoundation.sample.octonaut.xplat.feat.notificationsTab.api.NotificationsTab

interface ScreenFactory {
    fun homeTab(): HomeTab
    fun exploreTab(): ExploreTab
    fun notificationsTab(): NotificationsTab
}


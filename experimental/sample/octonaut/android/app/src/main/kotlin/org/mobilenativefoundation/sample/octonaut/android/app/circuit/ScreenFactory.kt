package org.mobilenativefoundation.sample.octonaut.android.app.circuit

import org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.api.HomeTab

interface ScreenFactory {
    fun homeTab(): HomeTab
}


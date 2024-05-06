package monster.scoop.android.app.circuit

import monster.scoop.xplat.feat.homeTab.api.HomeTab

interface ScreenFactory {
    fun homeTab(): HomeTab
}


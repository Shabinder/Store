package monster.scoop.android.app.circuit

import me.tatarka.inject.annotations.Inject
import monster.scoop.xplat.feat.homeTab.api.HomeTab
import monster.scoop.xplat.foundation.di.UserScope

@Inject
@UserScope
class ScoopScreenFactory(
    private val homeTab: HomeTab
) : ScreenFactory {
    override fun homeTab(): HomeTab = homeTab
}
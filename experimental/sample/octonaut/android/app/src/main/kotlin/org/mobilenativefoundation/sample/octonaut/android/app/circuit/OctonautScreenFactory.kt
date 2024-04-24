package org.mobilenativefoundation.sample.octonaut.android.app.circuit

import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.feat.exploreTab.api.ExploreTab
import org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.api.HomeTab
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.di.api.UserScope

@Inject
@UserScope
class OctonautScreenFactory(
    private val homeTab: HomeTab,
    private val exploreTab: ExploreTab
) : ScreenFactory {
    override fun homeTab(): HomeTab = homeTab
    override fun exploreTab(): ExploreTab = exploreTab
}
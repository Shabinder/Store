package org.mobilenativefoundation.sample.octonaut.android.app.circuit

import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.feat.NotificationsTabUi
import org.mobilenativefoundation.sample.octonaut.xplat.feat.exploreTab.api.ExploreTab
import org.mobilenativefoundation.sample.octonaut.xplat.feat.exploreTab.impl.ExploreTabUi
import org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.api.HomeTab
import org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.impl.HomeTabUi
import org.mobilenativefoundation.sample.octonaut.xplat.feat.notificationsTab.api.NotificationsTab
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.di.api.UserScope

@Inject
@UserScope
class OctonautUiFactory(
    private val homeTabUi: HomeTabUi,
    private val exploreTabUi: ExploreTabUi,
    private val notificationsTabUi: NotificationsTabUi
) : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is HomeTab -> homeTabUi
            is ExploreTab -> exploreTabUi
            is NotificationsTab -> notificationsTabUi
            else -> null
        }
    }
}
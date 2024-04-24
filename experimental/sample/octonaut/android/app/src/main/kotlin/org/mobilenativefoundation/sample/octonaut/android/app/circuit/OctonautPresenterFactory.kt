package org.mobilenativefoundation.sample.octonaut.android.app.circuit

import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.feat.NotificationsTabPresenter
import org.mobilenativefoundation.sample.octonaut.xplat.feat.exploreTab.api.ExploreTab
import org.mobilenativefoundation.sample.octonaut.xplat.feat.exploreTab.impl.ExploreTabPresenter
import org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.api.HomeTab
import org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.impl.HomeTabPresenter
import org.mobilenativefoundation.sample.octonaut.xplat.feat.notificationsTab.api.NotificationsTab
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.di.api.UserScope

@Inject
@UserScope
class OctonautPresenterFactory(
    private val homeTabPresenter: HomeTabPresenter,
    private val exploreTabPresenter: ExploreTabPresenter,
    private val notificationsTabPresenter: NotificationsTabPresenter
) : Presenter.Factory {
    override fun create(screen: Screen, navigator: Navigator, context: CircuitContext): Presenter<*>? {
        return when (screen) {
            is HomeTab -> homeTabPresenter
            is ExploreTab -> exploreTabPresenter
            is NotificationsTab -> notificationsTabPresenter
            else -> null
        }
    }

}
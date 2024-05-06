package monster.scoop.android.app.circuit

import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Inject
import monster.scoop.xplat.feat.homeTab.api.HomeTab
import monster.scoop.xplat.feat.homeTab.impl.HomeTabPresenter
import monster.scoop.xplat.foundation.di.UserScope

@Inject
@UserScope
class ScoopPresenterFactory(
    private val homeTabPresenterFactory: (navigator: Navigator) -> HomeTabPresenter
) : Presenter.Factory {
    override fun create(screen: Screen, navigator: Navigator, context: CircuitContext): Presenter<*>? {
        return when (screen) {
            is HomeTab -> homeTabPresenterFactory(navigator)
            else -> null
        }
    }

}
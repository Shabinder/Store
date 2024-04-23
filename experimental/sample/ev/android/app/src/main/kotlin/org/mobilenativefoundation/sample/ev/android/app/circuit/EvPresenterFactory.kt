package org.mobilenativefoundation.sample.ev.android.app.circuit

import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.ev.xplat.feat.chargingStationsTab.api.ChargingStationsTab
import org.mobilenativefoundation.sample.ev.xplat.foundation.di.api.UserScope

@Inject
@UserScope
class EvPresenterFactory(
    private val chargingStationsTabPresenter: ChargingStationsTab.Presenter
) : Presenter.Factory {
    override fun create(screen: Screen, navigator: Navigator, context: CircuitContext): Presenter<*>? {
        return when (screen) {
            is ChargingStationsTab -> chargingStationsTabPresenter
            else -> null
        }
    }

}
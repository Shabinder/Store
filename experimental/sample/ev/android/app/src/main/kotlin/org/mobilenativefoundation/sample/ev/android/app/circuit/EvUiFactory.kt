package org.mobilenativefoundation.sample.ev.android.app.circuit

import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.ev.xplat.feat.chargingStationsTab.api.ChargingStationsTab
import org.mobilenativefoundation.sample.ev.xplat.foundation.di.api.UserScope

@Inject
@UserScope
class EvUiFactory(
    private val chargingStationsTabUi: ChargingStationsTab.Ui
) : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is ChargingStationsTab -> ui<ChargingStationsTab.State> { state, modifier ->
                chargingStationsTabUi.Content(state, modifier)
            }

            else -> null
        }
    }
}
package org.mobilenativefoundation.sample.octonaut.android.app.circuit

import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.api.HomeTab
import org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.impl.HomeTabUi
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.di.api.UserScope

@Inject
@UserScope
class OctonautUiFactory(
    private val homeTabUi: HomeTabUi
) : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is HomeTab -> homeTabUi
            else -> null
        }
    }
}
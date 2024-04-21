package org.mobilenativefoundation.sample.ev.xplat.foundation.di.impl

import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.Inject

@Inject
class EvUiFactory(

) : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {

            else -> null
        }
    }
}
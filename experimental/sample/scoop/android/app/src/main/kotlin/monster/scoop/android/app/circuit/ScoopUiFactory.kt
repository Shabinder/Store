package monster.scoop.android.app.circuit

import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.Inject
import monster.scoop.xplat.foundation.di.UserScope

@Inject
@UserScope
class ScoopUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            else -> null
        }
    }
}
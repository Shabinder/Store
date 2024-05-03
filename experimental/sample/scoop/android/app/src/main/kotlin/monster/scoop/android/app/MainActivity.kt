package monster.scoop.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import me.tatarka.inject.annotations.Inject
import monster.scoop.android.app.di.CoreComponent
import monster.scoop.android.app.di.create


@Inject
class MainActivity : ComponentActivity() {

    private val coreComponent: CoreComponent by lazy {
        CoreComponent.create()
    }

    private val circuit: Circuit by lazy {
        Circuit.Builder()
            .addPresenterFactory(coreComponent.presenterFactory)
            .addUiFactory(coreComponent.uiFactory)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CircuitCompositionLocals(circuit) {
            }
        }
    }
}

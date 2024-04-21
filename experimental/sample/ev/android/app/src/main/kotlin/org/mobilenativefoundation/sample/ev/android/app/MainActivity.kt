package org.mobilenativefoundation.sample.ev.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.ev.android.app.di.RealCoreComponent
import org.mobilenativefoundation.sample.ev.android.app.di.create
import org.mobilenativefoundation.sample.ev.xplat.foundation.di.api.CoreComponent

@Inject
class MainActivity : ComponentActivity() {
    private val coreComponent: CoreComponent by lazy {
        RealCoreComponent.create()
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
                Text("EV")
            }
        }
    }
}

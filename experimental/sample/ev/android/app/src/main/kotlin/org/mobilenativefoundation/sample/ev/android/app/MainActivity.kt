package org.mobilenativefoundation.sample.ev.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.ev.android.app.di.CoreComponent
import org.mobilenativefoundation.sample.ev.android.app.di.create

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
                val chargingStationsTab = coreComponent.screenFactory.chargingStationsTab

                val activeScreen = remember { mutableStateOf<Screen>(chargingStationsTab) }

                Scaffold { paddingValues ->
                    CircuitContent(
                        screen = activeScreen.value,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}

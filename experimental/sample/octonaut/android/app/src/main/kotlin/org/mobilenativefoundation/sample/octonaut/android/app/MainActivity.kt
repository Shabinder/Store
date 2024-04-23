package org.mobilenativefoundation.sample.octonaut.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.android.app.di.CoreComponent
import org.mobilenativefoundation.sample.octonaut.android.app.di.create
import org.mobilenativefoundation.sample.octonaut.android.app.theme.OctonautTheme
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.GetUserQuery


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

                val homeTab = coreComponent.screenFactory.homeTab()
                val activeScreen = remember { mutableStateOf<Screen>(homeTab) }

                OctonautTheme {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        LaunchedEffect(Unit) {
                            try {

                                coreComponent.userMarketContributor.contribute(GetUserQuery("matt-ramotar"))

                            } catch (error: Throwable) {
                                println("Error: ${error.stackTraceToString()}")
                            }
                        }


                        Scaffold(
                            bottomBar = {
                                BottomAppBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
                                    Icon(Icons.Default.Home, "")
                                    Icon(Icons.Default.Notifications, "")
                                    Icon(Icons.Default.Search, "")
                                    Icon(Icons.Default.Person, "")
                                }
                            }
                        ) { innerPadding ->

                            CircuitContent(
                                screen = activeScreen.value,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}

package org.mobilenativefoundation.sample.octonaut.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import coil3.annotation.ExperimentalCoilApi
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.android.app.di.CoreComponent
import org.mobilenativefoundation.sample.octonaut.android.app.di.create
import org.mobilenativefoundation.sample.octonaut.android.app.theme.OctonautTheme
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.GetUserQuery
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.ListNotificationsQueryParams
import kotlin.time.Duration.Companion.minutes


@OptIn(ExperimentalCoilApi::class)
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

                val homeTab = remember { coreComponent.screenFactory.homeTab() }
                val exploreTab = remember { coreComponent.screenFactory.exploreTab() }
                val activeScreen = remember { mutableStateOf<Screen>(homeTab) }

                OctonautTheme {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        LaunchedEffect(Unit) {
                            try {

                                coreComponent.userSupplier.supply(GetUserQuery("matt-ramotar"))

                                coreComponent.scheduledNotificationsSupplier.supply(
                                    ListNotificationsQueryParams(),
                                    5.minutes
                                )

                            } catch (error: Throwable) {
                                println("Error: ${error.stackTraceToString()}")
                            }
                        }


                        Scaffold(
                            bottomBar = {
                                BottomAppBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
                                    IconButton(
                                        onClick = {
                                            activeScreen.value = homeTab
                                        }
                                    ) {
                                        Icon(Icons.Default.Home, "")
                                    }

                                    IconButton(
                                        onClick = {}
                                    ) {
                                        Icon(Icons.Default.Notifications, "")
                                    }

                                    IconButton(
                                        onClick = {
                                            activeScreen.value = exploreTab
                                        }
                                    ) {
                                        Icon(Icons.Default.Search, "")
                                    }

                                    IconButton(
                                        onClick = {}
                                    ) {
                                        Icon(Icons.Default.Person, "")
                                    }
                                }
                            }
                        ) { innerPadding ->

                            CircuitContent(
                                screen = activeScreen.value,
                                modifier = Modifier.padding(innerPadding)
                                    .background(MaterialTheme.colorScheme.background)
                            )
                        }
                    }
                }
            }
        }
    }
}

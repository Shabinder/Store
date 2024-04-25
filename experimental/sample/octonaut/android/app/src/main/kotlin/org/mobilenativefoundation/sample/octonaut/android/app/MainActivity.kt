package org.mobilenativefoundation.sample.octonaut.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import coil3.annotation.ExperimentalCoilApi
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.*
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.android.app.di.CoreComponent
import org.mobilenativefoundation.sample.octonaut.android.app.di.create
import org.mobilenativefoundation.sample.octonaut.android.app.theme.OctonautTheme
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.GetUserQuery
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.ListNotificationsQueryParams
import kotlin.time.Duration.Companion.minutes


@OptIn(ExperimentalCoilApi::class, ExperimentalMaterial3Api::class)
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

            val homeTab = remember { coreComponent.screenFactory.homeTab() }
            val exploreTab = remember { coreComponent.screenFactory.exploreTab() }
            val notificationsTab = remember { coreComponent.screenFactory.notificationsTab() }

            val webViewUrlState = coreComponent.webViewUrlStateHolder.url.collectAsState()

            val webViewState = webViewUrlState.value?.let { rememberWebViewState(it) }


            val backStack = rememberSaveableBackStack(root = homeTab)
            val navigator = rememberCircuitNavigator(backStack)

            CircuitCompositionLocals(circuit) {


                OctonautTheme {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LaunchedEffect(Unit) {
                            try {
                                coreComponent.currentUserSupplier.supply(GetUserQuery("matt-ramotar"))
                                coreComponent.scheduledNotificationsSupplier.supply(
                                    ListNotificationsQueryParams(),
                                    5.minutes
                                )
                                coreComponent.feedSupplier.supply(Unit)

                            } catch (error: Throwable) {
                                println("Error: ${error.stackTraceToString()}")
                            }
                        }


                        Scaffold(
                            bottomBar = {
                                BottomAppBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
                                    IconButton(
                                        onClick = {
                                            navigator.goTo(homeTab)
                                        }
                                    ) {
                                        Icon(Icons.Default.Home, "")
                                    }

                                    IconButton(
                                        onClick = {
                                            navigator.goTo(notificationsTab)
                                        }
                                    ) {
                                        Icon(Icons.Default.Notifications, "")
                                    }

                                    IconButton(
                                        onClick = {
                                            navigator.goTo(exploreTab)
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

                            NavigableCircuitContent(
                                navigator,
                                backStack,
                                modifier = Modifier.padding(innerPadding)
                                    .background(MaterialTheme.colorScheme.background),
                                decoration = NavigatorDefaults.EmptyDecoration
                            )
                        }

                        webViewState?.let {
                            val webViewNavigator = WebViewNavigator(rememberCoroutineScope())

                            Box(modifier = Modifier.fillMaxSize()) {
                                Column {
                                    TopAppBar(
                                        title = {
                                            Text(webViewState.pageTitle?.let { "${it}..." } ?: "",
                                                style = MaterialTheme.typography.labelLarge,
                                                maxLines = 1)
                                        },
                                        navigationIcon = {
                                            IconButton(onClick = {
                                                if (webViewNavigator.canGoBack) {
                                                    webViewNavigator.navigateBack()
                                                } else {
                                                    coreComponent.webViewUrlStateHolder.url.value = null
                                                }

                                            }) {
                                                Icon(Icons.AutoMirrored.Default.ArrowBack, "")
                                            }
                                        }
                                    )
                                    WebView(
                                        it,
                                        modifier = Modifier.fillMaxSize(),
                                        captureBackPresses = true,
                                        navigator = webViewNavigator
                                    )
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}

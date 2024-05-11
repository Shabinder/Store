package monster.scoop.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.*
import me.tatarka.inject.annotations.Inject
import monster.scoop.android.app.di.CoreComponent
import monster.scoop.android.app.di.create
import monster.scoop.xplat.foundation.designSystem.theme.AppTheme


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

            val backStack = rememberSaveableBackStack(root = homeTab)
            val navigator = rememberCircuitNavigator(backStack)

            CircuitCompositionLocals(circuit) {

                AppTheme(true) {
                    Scaffold(
                        bottomBar = {
                            BottomAppBar {
                                Icon(Icons.TwoTone.Home, "Home")
                                Icon(Icons.TwoTone.Search, "Search")
                                Icon(Icons.TwoTone.Person, "Person")
                            }
                        }
                    ) { innerPadding ->
                        NavigableCircuitContent(
                            navigator,
                            backStack,
                            modifier = Modifier.padding(innerPadding).background(MaterialTheme.colorScheme.background),
                            decoration = NavigatorDefaults.EmptyDecoration
                        )

                    }
                }
            }
        }
    }
}

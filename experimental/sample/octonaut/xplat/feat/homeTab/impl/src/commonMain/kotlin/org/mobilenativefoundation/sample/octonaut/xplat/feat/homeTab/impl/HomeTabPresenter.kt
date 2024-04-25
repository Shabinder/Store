package org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.slack.circuit.runtime.Navigator
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.api.HomeTab
import org.mobilenativefoundation.sample.octonaut.xplat.feat.userProfile.api.LaunchUserProfileScreen
import org.mobilenativefoundation.sample.octonaut.xplat.feat.userProfile.api.UserProfileScreen
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.webview.WebViewUrlStateHolder

@Inject
class HomeTabPresenter(
    private val warehouse: HomeTabWarehouse,
    private val webViewUrlStateHolder: WebViewUrlStateHolder,
    @Assisted private val navigator: Navigator
) : HomeTab.Presenter {

    private fun on(event: HomeTab.Event) {
        when (event) {
            HomeTab.Event.Refresh -> warehouse.dispatch(HomeTabWarehouseAction.Refresh)
            is HomeTab.Event.OpenWebView -> {
                webViewUrlStateHolder.url.value = event.url
            }

            is HomeTab.Event.OpenDetailedView.Repository -> TODO()
            is HomeTab.Event.OpenDetailedView.User -> {
                val userLogin = event.uri.removePrefix("https://github.com/")
                navigator.goTo(LaunchUserProfileScreen(userLogin))
            }
        }
    }

    @Composable
    override fun present(): HomeTab.State {
        val warehouseState = warehouse.state.collectAsState()

        return warehouseState.value.let { state ->
            state.user?.let { user ->
                state.feed?.let { feed ->
                    HomeTab.State.Loaded(user, feed, ::on)
                }
            } ?: HomeTab.State.Loading(::on)
        }
    }
}
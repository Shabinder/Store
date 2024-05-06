package monster.scoop.xplat.feat.homeTab.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.slack.circuit.runtime.Navigator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import monster.scoop.xplat.feat.homeTab.api.HomeTab

@Inject
class HomeTabPresenter(
    private val dataLayer: HomeTabDataLayer,
    @Assisted private val navigator: Navigator,
) : HomeTab.Presenter {

    private val anchorPositionFlow = MutableStateFlow<Int>(1)

    init {
        dataLayer.startPaging(anchorPositionFlow)
    }

    @Composable
    override fun present(): HomeTab.State {
        val errors = dataLayer.errors.collectAsState()
        val warnings = dataLayer.warnings.collectAsState()
        val viewData = dataLayer.viewData.collectAsState()

        return HomeTab.State(
            stories = viewData.value.stories,
            loadingStatus = HomeTab.State.LoadingStatus(start = false, end = false), // TODO
            errors = errors.value.map { HomeTab.Error.SomethingWentWrong }, // TODO
            warnings = warnings.value.map { HomeTab.Warning.Offline }, // TODO
            eventSink = { event ->
                when (event) {
                    HomeTab.Event.Refresh -> dataLayer.refreshHomeTab()
                }
            }
        )
        { anchorPosition ->
            anchorPositionFlow.tryEmit(anchorPosition)
        }
    }
}
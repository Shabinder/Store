package monster.scoop.xplat.feat.homeTab.impl.ui


import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.tatarka.inject.annotations.Inject
import monster.scoop.xplat.feat.homeTab.api.HomeTab
import monster.scoop.xplat.feat.homeTab.impl.ui.compose.HomeTabFeed

@Inject
class HomeTabUi : HomeTab.Ui {
    @Composable
    override fun Content(state: HomeTab.State, modifier: Modifier) {
        Column {
            if (state.stories.isEmpty()) {
                // Show loading indicator
            } else {
                HomeTabFeed(state.stories)
            }
        }
    }
}

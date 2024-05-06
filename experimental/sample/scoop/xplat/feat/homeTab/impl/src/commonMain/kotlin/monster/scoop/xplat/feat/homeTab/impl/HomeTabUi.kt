package monster.scoop.xplat.feat.homeTab.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import coil3.compose.AsyncImage
import me.tatarka.inject.annotations.Inject
import monster.scoop.xplat.feat.homeTab.api.HomeTab
import monster.scoop.xplat.feat.homeTab.api.HomeTabStory

@Inject
class HomeTabUi : HomeTab.Ui {
    @Composable
    override fun Content(state: HomeTab.State, modifier: Modifier) {
        Column {
            HomeTabFeed(state.stories)
        }
    }

    @Composable
    private fun HomeTabFeed(stories: List<HomeTabStory>) {

        LazyColumn {

            stories.forEach { story ->
                when (story) {
                    is HomeTabStory.Loaded -> {
                        item {
                            HomeTabFeedItem(story)
                        }
                    }

                    HomeTabStory.Placeholder -> {
                        item {
                            HomeTabPlaceholderItem()
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun HomeTabFeedItem(story: HomeTabStory.Loaded) {
        Column {
            story.imageUrl?.let {
                AsyncImage(it, null)
            }
            Text(story.title, color = Color.White)
        }
    }

    @Composable
    private fun HomeTabPlaceholderItem() {
        Column {
            Text("Placeholder", color = Color.White)
        }
    }
}
package monster.scoop.xplat.feat.homeTab.impl.ui.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ReplyAll
import androidx.compose.material.icons.twotone.Archive
import androidx.compose.material.icons.twotone.Snooze
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import monster.scoop.xplat.feat.homeTab.api.HomeTabStory

@Composable
internal fun HomeTabFeed(stories: List<HomeTabStory>) {
    val (trendingStories, otherStories) = stories.partition { stories.indexOf(it) < 5 }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            TrendingStories(trendingStories.filterIsInstance<HomeTabStory.Loaded>())
        }

        items(otherStories) { story ->
            when (story) {
                is HomeTabStory.Loaded -> HomeTabFeedItem(story)
                HomeTabStory.Placeholder -> HomeTabPlaceholderItem()
            }
        }
    }
}

@Composable
private fun HomeTabFeedItem(story: HomeTabStory.Loaded) {
    Card(
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
    ) {
        SwipeableHomeTabFeedItem { isSnoozed, isArchived ->
            HomeTabStoryContent(story, isSnoozed = isSnoozed, isArchived = isArchived)
        }
    }
}

@Composable
private fun HomeTabPlaceholderItem() {
    Card(onClick = {}, modifier = Modifier, enabled = true) {
        Text("Placeholder")
    }
}


@Composable
private fun TrendingStories(stories: List<HomeTabStory.Loaded>) {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp))) {
        Text("Trending Stories", style = MaterialTheme.typography.headlineSmall)

        stories.forEachIndexed { index, story ->

            SwipeableHomeTabFeedItem { isSnoozed, isArchived ->

                HomeTabStoryContent(
                    story = story,
                    index = index,
                    divider = index != stories.lastIndex,
                    isSnoozed = isSnoozed,
                    isArchived = isArchived,
                    isTrending = true
                )
            }
        }
    }
}

@Composable
private fun HomeTabStoryContent(
    story: HomeTabStory.Loaded,
    modifier: Modifier = Modifier,
    index: Int = 0,
    divider: Boolean = false,
    isSnoozed: Boolean = false,
    isArchived: Boolean = false,
    isTrending: Boolean = false
) {
    val contentModifier = modifier
        .fillMaxWidth()
        .shadow(1.dp)
        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp))
        .padding(vertical = 16.dp, horizontal = 20.dp)
        .animateContentSize()

    if (isTrending) {
        Column(contentModifier) {
            TrendingStoryContent(story, index, divider, isSnoozed, isArchived)
        }
    } else {
        Row(contentModifier) {
            FeedItemContent(story, isSnoozed, isArchived, Modifier.weight(0.6f))
        }
    }
}

@Composable
private fun FeedItemContent(
    story: HomeTabStory.Loaded,
    isSnoozed: Boolean,
    isArchived: Boolean,
    modifier: Modifier = Modifier
) {

    HomeTabStoryInfo(story, isSnoozed, isArchived, modifier.padding(horizontal = 16.dp))

    story.imageUrl?.let {
        AsyncImage(it, null, modifier = Modifier.size(100.dp), contentScale = ContentScale.Crop)
    }
}

@Composable
private fun TrendingStoryContent(
    story: HomeTabStory.Loaded,
    index: Int,
    divider: Boolean,
    isSnoozed: Boolean,
    isArchived: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .padding(top = 2.dp)
                .size(52.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text((index + 1).toString())
        }

        HomeTabStoryInfo(
            story, isSnoozed, isArchived, modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxHeight()
        )
    }

    if (divider) {
        HorizontalDivider(modifier = Modifier.padding(start = (52 + 16).dp, top = 16.dp))
    }
}

@Composable
private fun HomeTabStoryInfo(
    story: HomeTabStory.Loaded,
    isSnoozed: Boolean,
    isArchived: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = story.title,
            style = MaterialTheme.typography.titleSmall
        )

        if (isSnoozed) {
            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(Color.SeaBuckthorn.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                text = "Snoozed until tomorrow",
                style = MaterialTheme.typography.labelLarge
            )
        }

        if (isArchived) {
            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(Color.Fern.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                text = "Archived",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun SwipeableHomeTabFeedItem(
    modifier: Modifier = Modifier,
    content: @Composable (isSnoozed: Boolean, isArchived: Boolean) -> Unit
) {
    var isSnoozed by rememberSaveable { mutableStateOf(false) }
    var isArchived by rememberSaveable { mutableStateOf(false) }

    val swipeActions = listOf(
        SwipeAction(
            icon = rememberVectorPainter(Icons.AutoMirrored.TwoTone.ReplyAll),
            background = Color.Perfume,
            onSwipe = { /* Handle reply action */ },
            isUndo = false
        ),
        SwipeAction(
            icon = rememberVectorPainter(Icons.TwoTone.Snooze),
            background = Color.SeaBuckthorn,
            onSwipe = { isSnoozed = !isSnoozed },
            isUndo = isSnoozed
        ),
        SwipeAction(
            icon = rememberVectorPainter(Icons.TwoTone.Archive),
            background = Color.Fern,
            onSwipe = { isArchived = !isArchived },
            isUndo = isArchived
        )
    )

    SwipeableActionsBox(
        modifier = modifier,
        startActions = swipeActions.take(1),
        endActions = swipeActions.drop(1),
        swipeThreshold = 40.dp,
        backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.surfaceColorAtElevation(40.dp)
    ) {
        content(isSnoozed, isArchived)
    }
}

private val Color.Companion.SeaBuckthorn get() = Color(0xFFF9A825)
private val Color.Companion.Fern get() = Color(0xFF66BB6A)
private val Color.Companion.Perfume get() = Color(0xFFD0BCFF)
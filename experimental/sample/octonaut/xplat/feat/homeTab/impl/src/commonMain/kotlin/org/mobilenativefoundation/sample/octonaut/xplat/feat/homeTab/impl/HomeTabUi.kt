package org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.impl

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.api.HomeTab

@Inject
class HomeTabUi : HomeTab.Ui {
    @Composable
    override fun Content(state: HomeTab.State, modifier: Modifier) {
        Column {
            when (state) {
                HomeTab.State.Initial -> Text("Initial")
                is HomeTab.State.Loaded -> LoadedContent(state)
                is HomeTab.State.Loading -> Text("Loading")
            }
        }
    }

    @Composable
    private fun LoadedContent(state: HomeTab.State.Loaded) {
        Text(state.user.login)

        val painter = rememberAsyncImagePainter(state.user.avatarUrl)

        Image(painter = painter, contentDescription = "", modifier = Modifier.size(60.dp).clip(CircleShape))

        state.user.repositories.forEach {
            Text(it)
        }
    }

}
package org.mobilenativefoundation.sample.octonaut.xplat.feat.exploreTab.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.feat.exploreTab.api.ExploreTab

@OptIn(ExperimentalMaterial3Api::class)
@Inject
class ExploreTabUi : ExploreTab.Ui {
    @Composable
    override fun Content(state: ExploreTab.State, modifier: Modifier) {
        Column {
            when (state) {
                ExploreTab.State.Initial -> Text("Initial")
                is ExploreTab.State.Loaded -> LoadedContent(state)
                is ExploreTab.State.Loading -> Text("Loading")
            }
        }
    }

    @Composable
    private fun LoadedContent(state: ExploreTab.State.Loaded) {

        SearchBar(
            query = state.searchInput ?: "",
            onQueryChange = { state.eventSink(ExploreTab.Event.UpdateSearchInput(it)) },
            onSearch = { state.eventSink(ExploreTab.Event.Search(it)) },
            onActiveChange = { _ -> },
            active = true,
            colors = SearchBarDefaults.colors(containerColor = Color.Transparent),
            modifier = Modifier.height(100.dp)
        ) {
            Text(state.searchInput.toString())
        }
    }

}
package org.mobilenativefoundation.sample.octonaut.xplat.feat

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.feat.notificationsTab.api.NotificationsTab

@Inject
class NotificationsTabUi : NotificationsTab.Ui {
    @Composable
    override fun Content(state: NotificationsTab.State, modifier: Modifier) {
        Column {
            when (state) {
                NotificationsTab.State.Initial -> Text("Initial")
                is NotificationsTab.State.Loaded -> LoadedContent(state)
                is NotificationsTab.State.Loading -> Text("Loading")
            }
        }
    }

    @Composable
    private fun LoadedContent(state: NotificationsTab.State.Loaded) {

        state.notifications.forEach {
            Text(it.url)
        }
    }
}
package org.mobilenativefoundation.sample.octonaut.xplat.feat.userProfile.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.feat.userProfile.api.UserProfileScreen


@Inject
class UserProfileScreenUi : UserProfileScreen.Ui {
    @Composable
    override fun Content(state: UserProfileScreen.State, modifier: Modifier) {
        Column {
            when (state) {
                UserProfileScreen.State.Initial -> Text("Initial")
                is UserProfileScreen.State.Loaded -> LoadedContent(state)
                is UserProfileScreen.State.Loading -> Text("Loading")
            }
        }
    }

    @Composable
    private fun LoadedContent(state: UserProfileScreen.State.Loaded) {
        Text(state.user.name)
    }
}
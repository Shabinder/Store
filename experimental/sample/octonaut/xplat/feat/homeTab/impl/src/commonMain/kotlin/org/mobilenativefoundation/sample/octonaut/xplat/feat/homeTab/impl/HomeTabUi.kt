package org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.feat.homeTab.api.HomeTab

@Inject
class HomeTabUi : HomeTab.Ui {
    @Composable
    override fun Content(state: HomeTab.State, modifier: Modifier) {
        Column {
            val headingText = when (state) {
                HomeTab.State.Initial -> "Initial"
                is HomeTab.State.Loaded -> state.user.login
                is HomeTab.State.Loading -> "Loading"
            }

            Text(headingText)
        }
    }

}
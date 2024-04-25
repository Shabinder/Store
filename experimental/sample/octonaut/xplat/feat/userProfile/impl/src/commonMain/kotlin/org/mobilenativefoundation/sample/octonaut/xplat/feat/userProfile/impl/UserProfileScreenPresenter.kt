package org.mobilenativefoundation.sample.octonaut.xplat.feat.userProfile.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.feat.userProfile.api.UserProfileScreen

@Inject
class UserProfileScreenPresenter(
    private val screen: UserProfileScreen,
    private val warehouse: UserProfileScreenWarehouse
) : UserProfileScreen.Presenter {

    init {
        warehouse.dispatch(UserProfileScreenWarehouseAction.LoadUser(screen.login))
    }

    private fun on(event: UserProfileScreen.Event) {
        when (event) {
            UserProfileScreen.Event.Follow -> {
                // TODO
            }

            UserProfileScreen.Event.Unfollow -> {
                // TODO
            }
        }
    }

    @Composable
    override fun present(): UserProfileScreen.State {
        val warehouseState = warehouse.state.collectAsState()

        return UserProfileScreen.State.Loading(::on)
    }
}
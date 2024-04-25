package org.mobilenativefoundation.sample.octonaut.xplat.feat.userProfile.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.feat.userProfile.api.UserProfileScreen

@Inject
class UserProfileScreenPresenter(
    @Assisted private val login: String,
    warehouseFactory: (login: String) -> UserProfileScreenWarehouseFactory
) : UserProfileScreen.Presenter {

    private val warehouse = warehouseFactory(login).create()

    init {
        warehouse.dispatch(UserProfileScreenWarehouseAction.LoadUser(login))
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

        return warehouseState.value.user?.let {
            UserProfileScreen.State.Loaded(it, ::on)
        } ?: UserProfileScreen.State.Loading(::on)
    }
}
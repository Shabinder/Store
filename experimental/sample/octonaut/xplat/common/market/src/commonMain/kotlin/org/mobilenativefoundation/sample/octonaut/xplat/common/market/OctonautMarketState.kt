package org.mobilenativefoundation.sample.octonaut.xplat.common.market

import org.mobilenativefoundation.market.Market
import org.mobilenativefoundation.sample.octonaut.xplat.domain.feed.api.FeedState
import org.mobilenativefoundation.sample.octonaut.xplat.domain.notifications.api.NotificationsState
import org.mobilenativefoundation.sample.octonaut.xplat.domain.repository.api.RepositoriesState
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.CurrentUserState
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.User
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.UsersState

data class OctonautMarketState(
    val currentUser: CurrentUserState = CurrentUserState(),
    val users: UsersState = UsersState(),
    val notifications: NotificationsState = NotificationsState(),
    val feed: FeedState = FeedState(),
    val repositories: RepositoriesState = RepositoriesState(),
) : Market.State



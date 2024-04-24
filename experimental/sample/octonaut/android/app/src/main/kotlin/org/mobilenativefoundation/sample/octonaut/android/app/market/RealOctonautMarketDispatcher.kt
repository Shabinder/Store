package org.mobilenativefoundation.sample.octonaut.android.app.market

import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.OctonautMarketAction
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.OctonautMarketDispatcher
import org.mobilenativefoundation.sample.octonaut.xplat.domain.notifications.api.Notification
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.User

@Inject
class RealOctonautMarketDispatcher(
    private val mutableMarket: MutableOctonautMarket
) : OctonautMarketDispatcher {
    override fun dispatch(action: OctonautMarketAction) {
        val prevState = mutableMarket.state.value
        val nextState = when (action) {
            is OctonautMarketAction.UpdateUser -> {
                prevState.copy(
                    user = action.user?.let {
                        User(
                            id = it.id,
                            email = it.email,
                            name = it.name ?: "",
                            login = it.login,
                            avatarUrl = it.avatarUrl.toString(),
                            repositories = it.repositories.nodes?.mapNotNull { repo -> repo?.id } ?: emptyList(),
                            starredRepositories = it.starredRepositories.nodes?.mapNotNull { repo -> repo?.id }
                                ?: emptyList(),
                            organizations = it.organizations.nodes?.mapNotNull { org -> org?.id } ?: emptyList(),
                            pinnedItems = it.pinnedItems.let { pinnedItems -> List(pinnedItems.totalCount) { "" } },
                            socialAccounts = it.socialAccounts.nodes?.mapNotNull { socialAccount ->
                                socialAccount?.let {
                                    User.SocialAccount(
                                        socialAccount.displayName,
                                        socialAccount.provider.name
                                    )
                                }
                            } ?: emptyList()
                        )
                    }
                )
            }

            is OctonautMarketAction.UpdateNotifications -> {
                prevState.copy(
                    notifications = action.notifications.notifications.map {
                        Notification(
                            it.id,
                            it.unread,
                            it.updatedAt,
                            it.lastReadAt,
                            it.url,
                            it.subscriptionUrl
                        )
                    }
                )
            }
        }

        mutableMarket.updateState(nextState)
    }
}
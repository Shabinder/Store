package org.mobilenativefoundation.sample.octonaut.android.app.market.reducers

import org.mobilenativefoundation.market.Market
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.OctonautMarketAction
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.OctonautMarketState
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.User

val currentUserReducer: Market.Reducer<OctonautMarketState, OctonautMarketAction> =
    Market.Reducer { state, action ->

        if (action is OctonautMarketAction.UpdateCurrentUser) {
            state.copy(currentUser = state.currentUser.copy(
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
            ))
        } else {
            state
        }
    }
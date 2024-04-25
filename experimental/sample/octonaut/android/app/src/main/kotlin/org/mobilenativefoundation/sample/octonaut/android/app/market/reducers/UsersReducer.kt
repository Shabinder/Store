package org.mobilenativefoundation.sample.octonaut.android.app.market.reducers

import org.mobilenativefoundation.market.Market
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.OctonautMarketAction
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.OctonautMarketState
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.User
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.UsersState

val usersReducer: Market.Reducer<OctonautMarketState, OctonautMarketAction> = Market.Reducer { state, action ->

    if (action is OctonautMarketAction.AddUser) {
        val user = User(
            id = action.user.id,
            email = action.user.email,
            name = action.user.name ?: "",
            login = action.user.login,
            avatarUrl = action.user.avatarUrl.toString(),
            repositories = action.user.repositories.nodes?.mapNotNull { repo -> repo?.id } ?: emptyList(),
            starredRepositories = action.user.starredRepositories.nodes?.mapNotNull { repo -> repo?.id }
                ?: emptyList(),
            organizations = action.user.organizations.nodes?.mapNotNull { org -> org?.id } ?: emptyList(),
            pinnedItems = action.user.pinnedItems.let { pinnedItems -> List(pinnedItems.totalCount) { "" } },
            socialAccounts = action.user.socialAccounts.nodes?.mapNotNull { socialAccount ->
                socialAccount?.let {
                    User.SocialAccount(
                        socialAccount.displayName,
                        socialAccount.provider.name
                    )
                }
            } ?: emptyList()
        )

        val allIds = if (user.id !in state.users.byId) {
            val copy = state.users.allIds.toMutableList()
            copy.add(action.user.id)
            copy
        } else {
            state.users.allIds
        }

        val byId = state.users.byId.toMutableMap()
        byId[user.id] = user

        val loginToId = allIds.mapNotNull { id -> byId[id] }.associate { it.login to it.id }

        val usersState = UsersState(
            byId = byId,
            allIds = allIds,
            loginToId = loginToId
        )

        state.copy(
            users = usersState
        )
    } else {
        state
    }
}
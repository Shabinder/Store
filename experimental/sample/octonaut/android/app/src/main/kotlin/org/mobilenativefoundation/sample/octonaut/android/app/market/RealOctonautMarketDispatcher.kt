package org.mobilenativefoundation.sample.octonaut.android.app.market

import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.OctonautMarketAction
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.OctonautMarketDispatcher
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
                            login = it.name ?: "",
                            repositoryIds = it.repositories.nodes?.mapNotNull { it?.id } ?: emptyList()
                        )
                    }
                )
            }
        }

        mutableMarket.updateState(nextState)
    }
}
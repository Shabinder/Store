package org.mobilenativefoundation.sample.octonaut.android.app.market.reducers

import org.mobilenativefoundation.market.Market
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.OctonautMarketAction
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.OctonautMarketState
import org.mobilenativefoundation.sample.octonaut.xplat.domain.repository.api.RepositoriesState
import org.mobilenativefoundation.sample.octonaut.xplat.domain.repository.api.Repository

val repositoriesReducer: Market.Reducer<OctonautMarketState, OctonautMarketAction> = Market.Reducer { state, action ->

    if (action is OctonautMarketAction.AddRepository) {
        val repository = Repository(
            action.repository.id,
            action.repository.name
        )

        val allIds = if (repository.id !in state.repositories.byId) {
            val copy = state.repositories.allIds.toMutableList()
            copy.add(repository.id)
            copy
        } else {
            state.repositories.allIds
        }

        val byId = state.repositories.byId.toMutableMap()
        byId[repository.id] = repository

        val repositoriesState = RepositoriesState(
            byId = byId,
            allIds = allIds,
        )

        state.copy(
            repositories = repositoriesState
        )
    } else {
        state
    }
}
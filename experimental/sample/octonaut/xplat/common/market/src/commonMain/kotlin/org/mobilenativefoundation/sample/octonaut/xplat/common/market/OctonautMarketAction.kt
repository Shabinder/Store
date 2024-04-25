package org.mobilenativefoundation.sample.octonaut.xplat.common.market

import org.mobilenativefoundation.market.Market
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.Feed
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.GetRepositoryQuery
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.GetUserQuery
import org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api.ListNotificationsResponse

sealed interface OctonautMarketAction : Market.Action {

    data class UpdateCurrentUser(
        val user: GetUserQuery.User?
    ): OctonautMarketAction

    data class AddUser(
        val user: GetUserQuery.User
    ) : OctonautMarketAction

    data class UpdateNotifications(
        val notifications: ListNotificationsResponse
    ) : OctonautMarketAction

    data class AddNotifications(
        val notifications: ListNotificationsResponse
    ): OctonautMarketAction

    data class UpdateFeed(
        val feed: Feed
    ): OctonautMarketAction

    data class AddRepository(
        val repository: GetRepositoryQuery.Repository
    ): OctonautMarketAction
}
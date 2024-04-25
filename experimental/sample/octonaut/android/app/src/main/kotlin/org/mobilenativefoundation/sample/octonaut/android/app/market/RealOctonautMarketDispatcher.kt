package org.mobilenativefoundation.sample.octonaut.android.app.market

import me.tatarka.inject.annotations.Inject
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.OctonautMarketAction
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.OctonautMarketDispatcher
import org.mobilenativefoundation.sample.octonaut.xplat.domain.feed.api.*
import org.mobilenativefoundation.sample.octonaut.xplat.domain.notifications.api.Notification
import org.mobilenativefoundation.sample.octonaut.xplat.domain.notifications.api.NotificationsState
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.User
import org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api.UsersState

@Inject
class RealOctonautMarketDispatcher(
    private val mutableMarket: MutableOctonautMarket
) : OctonautMarketDispatcher {
    override fun dispatch(action: OctonautMarketAction) {
        val prevState = mutableMarket.state.value
        val nextState = when (action) {
            is OctonautMarketAction.UpdateCurrentUser -> {
                prevState.copy(
                    currentUser = action.user?.let {
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

                val notifications = action.notifications.notifications.map {
                    Notification(
                        it.id,
                        it.unread,
                        it.updatedAt,
                        it.lastReadAt,
                        it.url,
                        it.subscriptionUrl
                    )
                }

                val byId = notifications.associateBy { it.id }
                val allIds = notifications.map { it.id }

                val notificationsState = NotificationsState(
                    byId = byId,
                    allIds = allIds
                )

                prevState.copy(
                    notifications = notificationsState
                )
            }

            is OctonautMarketAction.UpdateFeed -> {

                val id = action.feed.id

                val entries = action.feed.entry.map {
                    Entry(
                        it.id,
                        it.published,
                        it.updated,
                        it.link.href,
                        it.title,
                        it.author.uri,
                        it.thumbnail.url
                    )
                }

                val entriesState = EntriesState(
                    byId = entries.associateBy { it.id },
                    allIds = entries.map { it.id }
                )

                val links = action.feed.link.map {
                    Link(
                        it.type,
                        it.rel,
                        it.href
                    )
                }

                val linksState = LinksState(
                    byHref = links.associateBy { it.href },
                    allHrefs = links.map { it.href }
                )

                val thumbnails = action.feed.entry.map {
                    Thumbnail(
                        it.thumbnail.height,
                        it.thumbnail.width,
                        it.thumbnail.url
                    )
                }

                val thumbnailsState = ThumbnailsState(
                    byUrl = thumbnails.associateBy { it.url },
                    allUrls = thumbnails.map { it.url }
                )

                val feedState = FeedState(
                    id = id,
                    entries = entriesState,
                    links = linksState,
                    thumbnails = thumbnailsState
                )


                prevState.copy(
                    feed = feedState
                )
            }

            is OctonautMarketAction.AddUser -> {
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

                val allIds = if (user.id !in prevState.users.byId) {
                    val copy = prevState.users.allIds.toMutableList()
                    copy.add(action.user.id)
                    copy
                } else {
                    prevState.users.allIds
                }

                val byId = prevState.users.byId.toMutableMap()
                byId[user.id] = user

                val loginToId = allIds.mapNotNull { id -> byId[id] }.associate { it.login to it.id }

                val usersState = UsersState(
                    byId = byId,
                    allIds = allIds,
                    loginToId = loginToId
                )

                prevState.copy(
                    users = usersState
                )
            }

            is OctonautMarketAction.AddNotifications -> {


                val notifications = action.notifications.notifications.map {
                    Notification(
                        it.id,
                        it.unread,
                        it.updatedAt,
                        it.lastReadAt,
                        it.url,
                        it.subscriptionUrl
                    )
                }

                val byId = prevState.notifications.byId.toMutableMap()
                val allIds = prevState.notifications.allIds.toMutableList()

                notifications.forEach {
                    if (it.id !in byId) {
                        allIds.add(it.id)
                    }

                    byId[it.id] = it
                }

                val notificationsState = NotificationsState(
                    byId = byId,
                    allIds = allIds
                )

                prevState.copy(
                    notifications = notificationsState
                )

            }
        }

        println("UPDATING STATE: $prevState to $nextState")

        mutableMarket.updateState(nextState)
    }
}
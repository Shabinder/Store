package org.mobilenativefoundation.sample.octonaut.android.app.market.reducers

import org.mobilenativefoundation.market.Market
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.OctonautMarketAction
import org.mobilenativefoundation.sample.octonaut.xplat.common.market.OctonautMarketState
import org.mobilenativefoundation.sample.octonaut.xplat.domain.feed.api.*

val feedReducer: Market.Reducer<OctonautMarketState, OctonautMarketAction> = Market.Reducer { state, action ->

    if (action is OctonautMarketAction.UpdateFeed) {
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

        state.copy(
            feed = feedState
        )
    } else {
        state
    }
}
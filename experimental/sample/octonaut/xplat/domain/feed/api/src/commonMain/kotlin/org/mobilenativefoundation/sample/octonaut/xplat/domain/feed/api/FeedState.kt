package org.mobilenativefoundation.sample.octonaut.xplat.domain.feed.api

data class FeedState(
    val id: String? = null,
    val entries: EntriesState = EntriesState(),
    val links: LinksState = LinksState(),
    val thumbnails: ThumbnailsState = ThumbnailsState()
)

data class EntriesState(
    val byId: Map<String, Entry> = mapOf(),
    val allIds: List<String> = emptyList()
)

data class LinksState(
    val byHref: Map<String, Link> = mapOf(),
    val allHrefs: List<String> = emptyList()
)

data class ThumbnailsState(
    val byUrl: Map<String, Thumbnail> = mapOf(),
    val allUrls: List<String> = emptyList()
)
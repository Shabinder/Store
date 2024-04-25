package org.mobilenativefoundation.sample.octonaut.xplat.domain.feed.api


data class Feed(
    val id: String,
    val linkHrefs: List<String>,
    val entryIds: List<String>,
)

data class Link(
    val type: String,
    val rel: String,
    val href: String
)

data class Entry(
    val id: String,
    val published: String,
    val updated: String,
    val linkHref: String,
    val title: String,
    val authorUri: String,
    val thumbnailUrl: String
)

data class Author(
    val name: String,
    val uri: String,
    val email: String,
)

data class Thumbnail(
    val height: Int,
    val width: Int,
    val url: String
)
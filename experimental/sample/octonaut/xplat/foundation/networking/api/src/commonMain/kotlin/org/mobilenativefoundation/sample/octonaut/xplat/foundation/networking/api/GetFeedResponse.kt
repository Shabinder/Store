package org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
data class GetFeedResponse(
    @SerialName("timeline_url")
    val timelineUrl: String,
    @SerialName("user_url")
    val userUrl: String,
    @SerialName("current_user_public_url")
    val currentUserPublicUrl: String,
    @SerialName("current_user_url")
    val currentUserUrl: String,
    @SerialName("current_user_actor_url")
    val currentUserActorUrl: String,
    @SerialName("current_user_organization_url")
    val currentUserOrganizationUrl: String,
    @SerialName("current_user_organization_urls")
    val currentUserOrganizationUrls: List<String>,
    @SerialName("security_advisories_url")
    val securityAdvisoriesUrl: String,
    @SerialName("repository_discussions_url")
    val repositoryDiscussionsUrl: String,
    @SerialName("repository_discussions_category_url")
    val repositoryDiscussionsCategoryUrl: String,
)

@Serializable
data class Links(
    val timeline: LinkWithType,
    val user: LinkWithType,
    @SerialName("security_advisories")
    val securityAdvisories: LinkWithType?,
    @SerialName("current_user")
    val currentUser: LinkWithType?,
    @SerialName("current_user_public")
    val currentUserPublic: LinkWithType?,
    @SerialName("current_user_actor")
    val currentUserActor: LinkWithType?,
    @SerialName("current_user_organization")
    val currentUserOrganization: LinkWithType?,
    @SerialName("current_user_organizations")
    val currentUserOrganizations: List<LinkWithType>?,
    @SerialName("repository_discussions")
    val repositoryDiscussions: LinkWithType?,
    @SerialName("repository_discussions_category")
    val repositoryDiscussionsCategory: LinkWithType?
)

@Serializable
data class LinkWithType(
    val href: String,
    val type: String
)

@Serializable
@XmlSerialName("feed", namespace = "http://www.w3.org/2005/Atom", prefix = "")
data class Feed(
    @XmlSerialName("id", namespace = "http://www.w3.org/2005/Atom")
    @XmlElement(true)
    val id: String,
    @XmlElement(true)
    val link: List<Link>,
    @XmlElement(true)
    val title: String,
    @XmlElement(true)
    val updated: String,
    @XmlElement(true)
    val entry: List<Entry>
)

@Serializable
@XmlSerialName("link", namespace = "http://www.w3.org/2005/Atom", prefix = "")
data class Link(
    @XmlElement(false)
    @XmlSerialName("type", namespace = "http://www.w3.org/2005/Atom")
    val type: String,
    @XmlElement(false)
    @XmlSerialName("rel")
    val rel: String,
    @XmlElement(false)
    @XmlSerialName("href")
    val href: String
)

@Serializable
@XmlSerialName("entry", namespace = "http://www.w3.org/2005/Atom", prefix = "")
data class Entry(
    @XmlElement(true)
    val id: String,
    @XmlElement(true)
    val published: String,
    @XmlElement(true)
    val updated: String,
    @XmlElement(true)
    val link: Link,
    @XmlElement(true)
    val title: String,
    @XmlElement(true)
    val author: Author,
    @XmlElement(true)
    val thumbnail: Thumbnail,
    @XmlElement(true)
    val content: String
)

@Serializable
@XmlSerialName("author", namespace = "http://www.w3.org/2005/Atom", prefix = "")
data class Author(
    @XmlElement(true)
    @XmlSerialName("name", namespace = "http://www.w3.org/2005/Atom")
    val name: String,
    @XmlElement(true)
    @XmlSerialName("uri", namespace = "http://www.w3.org/2005/Atom")
    val uri: String,
    @XmlElement(true)
    @XmlSerialName("email", namespace = "http://www.w3.org/2005/Atom")
    val email: String? = null
)

@Serializable
@XmlSerialName("thumbnail", namespace = "http://search.yahoo.com/mrss/", prefix = "media")
data class Thumbnail(
    @XmlElement(false)
    val height: Int,
    @XmlElement(false)
    val width: Int,
    @XmlElement(false)
    val url: String
)
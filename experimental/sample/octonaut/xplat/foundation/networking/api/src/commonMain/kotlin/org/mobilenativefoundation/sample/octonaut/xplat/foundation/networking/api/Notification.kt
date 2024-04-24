package org.mobilenativefoundation.sample.octonaut.xplat.foundation.networking.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListNotificationsQueryParams(
    val all: Boolean = false,
    val participating: Boolean = false,
    val since: String? = null,
    val before: String? = null,
    val page: Int = 1,
    val perPage: Int = 50
)

@Serializable
data class ListNotificationsResponse(
    val notifications: List<Thread>
)

@Serializable
data class Thread(
    val id: String,
    // val repository: MinimalRepository,
    // val subject: Subject,
    // val reason: String,
    val unread: Boolean,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("last_read_at")
    val lastReadAt: String?,
    val url: String,
    @SerialName("subscription_url")
    val subscriptionUrl: String
)

@Serializable
data class MinimalRepository(
    val id: Int,
    val nodeId: String,
    val name: String,
    val fullName: String,
    val owner: SimpleUser,
    val private: Boolean,
    val htmlUrl: String,
    val description: String?,
    val fork: Boolean,
    val url: String,
    val archiveUrl: String,
    val assigneesUrl: String,
    val blobsUrl: String,
    val branchesUrl: String,
    val collaboratorsUrl: String,
    val commentsUrl: String,
    val commitsUrl: String,
    val compareUrl: String,
    val contentsUrl: String,
    val contributorsUrl: String,
    val deploymentsUrl: String,
    val downloadsUrl: String,
    val eventsUrl: String,
    val forksUrl: String,
    val gitCommitsUrl: String,
    val gitRefsUrl: String,
    val gitTagsUrl: String,
    val gitUrl: String,
    val issueCommentUrl: String,
    val issueEventsUrl: String,
    val issuesUrl: String,
    val keysUrl: String,
    val labelsUrl: String,
    val languagesUrl: String,
    val mergesUrl: String,
    val milestonesUrl: String,
    val notificationsUrl: String,
    val pullsUrl: String,
    val releasesUrl: String,
    val sshUrl: String,
    val stargazersUrl: String,
    val statusesUrl: String,
    val subscribersUrl: String,
    val subscriptionUrl: String,
    val tagsUrl: String,
    val teamsUrl: String,
    val treesUrl: String,
    val cloneUrl: String,
    val mirrorUrl: String?,
    val hooksUrl: String,
    val svnUrl: String,
    val homepage: String?,
    val language: String?,
    val forksCount: Int,
    val stargazersCount: Int,
    val watchersCount: Int,
    val size: Int,
    val defaultBranch: String,
    val openIssuesCount: Int,
    val isTemplate: Boolean,
    val topics: List<String>,
    val hasIssues: Boolean,
    val hasProjects: Boolean,
    val hasWiki: Boolean,
    val hasPages: Boolean,
    val hasDownloads: Boolean,
    val hasDiscussions: Boolean,
    val archived: Boolean,
    val disabled: Boolean,
    val visibility: String,
    val pushedAt: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val permissions: Permissions?,
    val roleName: String,
    val tempCloneToken: String,
    val deleteBranchOnMerge: Boolean,
    val subscribersCount: Int,
    val networkCount: Int,
    val codeOfConduct: CodeOfConduct?,
    val license: License?,
    val forks: Int,
    val openIssues: Int,
    val watchers: Int,
    val allowForking: Boolean,
    val webCommitSignoffRequired: Boolean,
    val securityAndAnalysis: SecurityAndAnalysis?
)

@Serializable
data class SimpleUser(
    val name: String?,
    val email: String?,
    val login: String,
    val id: Int,
    val nodeId: String,
    val avatarUrl: String,
    val gravatarId: String?,
    val url: String,
    val htmlUrl: String,
    val followersUrl: String,
    val followingUrl: String,
    val gistsUrl: String,
    val starredUrl: String,
    val subscriptionsUrl: String,
    val organizationsUrl: String,
    val reposUrl: String,
    val eventsUrl: String,
    val receivedEventsUrl: String,
    val type: String,
    val siteAdmin: Boolean,
    val starredAt: String
)

@Serializable
data class Permissions(
    val admin: Boolean,
    val maintain: Boolean,
    val push: Boolean,
    val triage: Boolean,
    val pull: Boolean
)

@Serializable
data class CodeOfConduct(
    val key: String,
    val name: String,
    val url: String,
    val body: String,
    val htmlUrl: String?
)

@Serializable
data class License(
    val key: String,
    val name: String,
    val spdxId: String,
    val url: String,
    val nodeId: String
)

@Serializable
data class SecurityAndAnalysis(
    val advancedSecurity: AdvancedSecurity?,
    val dependabotSecurityUpdates: DependabotSecurityUpdates?,
    val secretScanning: SecretScanning?,
    val secretScanningPushProtection: SecretScanningPushProtection?
)

@Serializable
data class AdvancedSecurity(
    val status: String
)

@Serializable
data class DependabotSecurityUpdates(
    val status: String
)

@Serializable
data class SecretScanning(
    val status: String
)

@Serializable
data class SecretScanningPushProtection(
    val status: String
)

@Serializable
data class Subject(
    val title: String,
    val url: String,
    val latestCommentUrl: String,
    val type: String
)
package org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api

data class User(
    val id: String,
    val login: String,
    val name: String,
    val email: String,
    val avatarUrl: String,
    val repositories: List<String>,
    val starredRepositories: List<String>,
    val organizations: List<String>,
    val pinnedItems: List<String>,
    val socialAccounts: List<SocialAccount>
) {
    data class SocialAccount(
        val displayName: String,
        val provider: String
    )
}


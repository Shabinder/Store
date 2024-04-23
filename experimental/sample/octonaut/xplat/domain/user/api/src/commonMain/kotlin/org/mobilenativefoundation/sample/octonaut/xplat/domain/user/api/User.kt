package org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api

data class User(
    val id: String,
    val login: String,
    val name: String,
    val email: String,
    val repositoryIds: List<String>
)


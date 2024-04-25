package org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api

data class UsersState(
    val byId: Map<String, User> = mapOf(),
    val loginToId: Map<String, String> = mapOf(),
    val allIds: List<String> = emptyList()
)
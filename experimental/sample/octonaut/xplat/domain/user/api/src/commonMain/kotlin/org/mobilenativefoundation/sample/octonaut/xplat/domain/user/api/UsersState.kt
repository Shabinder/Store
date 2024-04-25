package org.mobilenativefoundation.sample.octonaut.xplat.domain.user.api

data class UsersState(
    val byId: Map<String, User> = mapOf(),
    val allIds: List<String> = emptyList()
)
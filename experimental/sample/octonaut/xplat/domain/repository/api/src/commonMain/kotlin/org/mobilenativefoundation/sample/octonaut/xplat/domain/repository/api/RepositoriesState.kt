package org.mobilenativefoundation.sample.octonaut.xplat.domain.repository.api

data class RepositoriesState(
    val byId: Map<String, Repository> = mapOf(),
    val allIds: List<String> = listOf(),
)

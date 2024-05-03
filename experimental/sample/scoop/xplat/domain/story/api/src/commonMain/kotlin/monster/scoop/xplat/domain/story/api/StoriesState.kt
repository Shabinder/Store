package monster.scoop.xplat.domain.story.api

data class StoriesState(
    val byId: Map<Int, Story> = mapOf(),
    val allIds: List<Int> = listOf()
)
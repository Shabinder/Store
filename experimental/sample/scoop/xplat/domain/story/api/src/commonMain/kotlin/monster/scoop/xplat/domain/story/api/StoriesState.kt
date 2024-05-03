package monster.scoop.xplat.domain.story.api

data class StoriesState(
    val byId: Map<Int, Story>,
    val allIds: List<Int>
)
package monster.scoop.xplat.domain.story.api

import kotlinx.datetime.LocalDateTime

data class Story(
    val id: Int,
    val title: String,
    val description: String,
    val url: String,
    val publicationDate: LocalDateTime,
    val authorId: Int,
    val storyThumbnailIds: List<Int>,
    val content: String
)

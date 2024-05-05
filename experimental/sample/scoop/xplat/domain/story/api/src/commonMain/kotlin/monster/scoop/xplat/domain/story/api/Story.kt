package monster.scoop.xplat.domain.story.api

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import monster.scoop.xplat.foundation.networking.api.fragment.StoryFields
import org.mobilenativefoundation.storex.paging.Identifiable

data class Story(
    override val id: Int,
    val title: String,
    val description: String,
    val url: String,
    val publicationDate: LocalDateTime,
    val authorId: Int?,
    val storyThumbnailIds: List<Int>,
    val content: String?
) : Identifiable<Int> {
    companion object {
        fun placeholder() = Story(
            id = -1,
            title = "",
            description = "",
            url = "",
            publicationDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            authorId = -1,
            storyThumbnailIds = emptyList(),
            content = ""
        )
    }
}

data class NetworkStory(
    override val id: Int,
    val data: StoryFields,
) : Identifiable<Int> {
    companion object {
        fun placeholder() = NetworkStory(
            id = -1,
            data = StoryFields(
                id = -1,
                title = "",
                description = "",
                url = "",
                publication_date = "",
                author = null,
                story_thumbnails = emptyList(),
                content = ""
            )
        )
    }
}
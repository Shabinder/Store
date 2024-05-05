package monster.scoop.xplat.common.market

import kotlinx.datetime.LocalDateTime
import monster.scoop.xplat.domain.story.api.NetworkStory
import monster.scoop.xplat.domain.story.api.StoriesError
import monster.scoop.xplat.domain.story.api.Story
import org.mobilenativefoundation.market.Market
import org.mobilenativefoundation.market.StatefulMarket
import org.mobilenativefoundation.market.StatefulMarket.PagingState.Data.Status

typealias StoryState = StatefulMarket.ItemState<Int, Story, StoriesError>

sealed interface ScoopAction : Market.Action {
    sealed interface Stories : ScoopAction {
        data class SetStories(val stories: List<Story>) : Stories
        data class PushStories(val stories: List<Story>) : Stories

        data class SetStory(val storyId: Int, val storyState: StoryState) : Stories
        data class UpdateStoryDataStatus(val status: StatefulMarket.ItemState.Data.Status) : Stories

        data class NormalizeStory(val story: NetworkStory) : Stories

        sealed interface Paging : Stories {
            data class SetInitial(val prefetchPosition: Int?, val anchorPosition: Int?) : Stories
            data class SetLoading(val prefetchPosition: Int?, val anchorPosition: Int?) : Stories
            data class SetError(val error: StoriesError, val prefetchPosition: Int?, val anchorPosition: Int?) : Stories

            data class SetData(
                val stories: List<Story>,
                val lastModified: LocalDateTime,
                val lastRefreshed: LocalDateTime,
                val status: Status<StoriesError>
            ) : Paging

            data class UpdateData(
                val stories: List<Story>? = null,
                val lastModified: LocalDateTime? = null,
                val lastRefreshed: LocalDateTime? = null,
                val status: Status<StoriesError>? = null
            ) : Paging

            data class NormalizeAll(
                val stories: List<NetworkStory>
            ) : Paging
        }
    }
}
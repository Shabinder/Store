package monster.scoop.xplat.common.market

import monster.scoop.xplat.domain.story.api.StoriesState
import org.mobilenativefoundation.market.Market

data class ScoopState(
    val stories: StoriesState = StoriesState()
) : Market.State




package monster.scoop.xplat.common.market

import monster.scoop.xplat.domain.story.api.StoriesState
import org.mobilenativefoundation.market.Market

data class ScoopMarketState(
    val stories: StoriesState
) : Market.State




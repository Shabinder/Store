package monster.scoop.xplat.common.market

import monster.scoop.xplat.domain.story.api.StoriesState
import org.mobilenativefoundation.market.StatefulMarket

data class ScoopState(
    override val subStates: Map<String, StatefulMarket.SubState> = subStates()
) : StatefulMarket.State

private fun subStates(): Map<String, StatefulMarket.SubState> = buildMap {
    "stories" to StoriesState()
}







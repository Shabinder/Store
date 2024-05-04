package monster.scoop.xplat.domain.story.api

import org.mobilenativefoundation.market.StatefulMarket

typealias StoriesState = StatefulMarket.NormalizedState<Int, Story, StoriesError>
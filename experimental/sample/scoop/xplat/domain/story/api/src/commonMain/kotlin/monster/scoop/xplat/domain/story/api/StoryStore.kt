package monster.scoop.xplat.domain.story.api

import monster.scoop.xplat.foundation.networking.api.GetStoryQuery
import org.mobilenativefoundation.store.store5.Store


typealias StoryStore = Store<GetStoryQuery, GetStoryQuery.Data>
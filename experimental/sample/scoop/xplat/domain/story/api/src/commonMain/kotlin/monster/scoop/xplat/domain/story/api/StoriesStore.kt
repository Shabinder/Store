package monster.scoop.xplat.domain.story.api

import monster.scoop.xplat.foundation.networking.api.GetStoriesQuery
import org.mobilenativefoundation.store.store5.Store


typealias StoriesStore = Store<GetStoriesQuery, GetStoriesQuery.Data>
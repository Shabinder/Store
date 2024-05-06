package monster.scoop.android.app.market.reducers

import monster.scoop.xplat.common.market.ScoopAction
import monster.scoop.xplat.common.market.ScoopState
import org.mobilenativefoundation.market.Market

val storiesReducer : Market.Reducer<ScoopState, ScoopAction> = Market.Reducer {state, action ->
    if (action is ScoopAction.Stories) {
        when (action) {
            is ScoopAction.Stories.NormalizeStory -> state // TODO
            is ScoopAction.Stories.Paging.NormalizeAll -> state // TODO
            is ScoopAction.Stories.Paging.SetData -> state // TODO
            is ScoopAction.Stories.Paging.UpdateData -> state // TODO
            is ScoopAction.Stories.PushStories -> state // TODO
            is ScoopAction.Stories.Paging.SetError -> state // TODO
            is ScoopAction.Stories.Paging.SetInitial -> state // TODO
            is ScoopAction.Stories.Paging.SetLoading -> state // TODO
            is ScoopAction.Stories.SetStories -> state // TODO
            is ScoopAction.Stories.SetStory -> state // TODO
            is ScoopAction.Stories.UpdateStoryDataStatus -> state // TODO
        }
    } else {
        state
    }
}
package monster.scoop.android.app.market

import monster.scoop.xplat.common.market.ScoopAction
import monster.scoop.xplat.common.market.ScoopState
import org.mobilenativefoundation.market.Market

typealias RootReducer = Market.Reducer<ScoopState, ScoopAction>
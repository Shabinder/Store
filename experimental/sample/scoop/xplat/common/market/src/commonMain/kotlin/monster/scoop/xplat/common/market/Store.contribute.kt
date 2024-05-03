package monster.scoop.xplat.common.market

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.mobilenativefoundation.market.Market
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.impl.extensions.fresh


fun <A : Market.Action, D : Market.Dispatcher<A>, K : Any, V : Any> Store<K, V>.contribute(
    key: K,
    marketDispatcher: D,
    coroutineDispatcher: CoroutineDispatcher,
    actionFactory: (V) -> A
) {

    val coroutineScope = CoroutineScope(coroutineDispatcher)
    coroutineScope.launch {
        val response = fresh(key)
        val marketAction = actionFactory(response)
        marketDispatcher.dispatch(marketAction)
    }
}
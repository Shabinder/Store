package org.mobilenativefoundation.market.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.mobilenativefoundation.market.Market
import org.mobilenativefoundation.market.MarketSupplier
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.impl.extensions.fresh

internal class RealMarketSupplier<K : Any, O : Any, A : Market.Action, D : Market.Dispatcher<A>>(
    coroutineDispatcher: CoroutineDispatcher,
    private val store: Store<K, O>,
    private val marketDispatcher: D,
    private val marketActionFactory: MarketActionFactory<O, A>
): MarketSupplier<K> {
    private val coroutineScope = CoroutineScope(coroutineDispatcher)

    override fun supply(key: K) {
        coroutineScope.launch {
            val storeOutput = store.fresh(key)
            val marketAction = marketActionFactory.create(storeOutput)
            marketDispatcher.dispatch(marketAction)
        }
    }
}

fun interface MarketActionFactory<O : Any, A : Market.Action> {
    fun create(storeOutput: O): A
}

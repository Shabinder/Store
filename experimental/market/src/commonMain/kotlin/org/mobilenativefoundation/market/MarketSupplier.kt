package org.mobilenativefoundation.market

import kotlinx.coroutines.CoroutineDispatcher
import org.mobilenativefoundation.market.impl.MarketActionFactory
import org.mobilenativefoundation.market.impl.RealMarketSupplier
import org.mobilenativefoundation.store.store5.Store

interface MarketSupplier<K : Any> {
    fun supply(key: K)

    companion object {
        fun <K : Any, O : Any, A : Market.Action, D : Market.Dispatcher<A>> from(
            coroutineDispatcher: CoroutineDispatcher,
            store: Store<K, O>,
            marketDispatcher: D,
            marketActionFactory: MarketActionFactory<O, A>
        ): MarketSupplier<K> {
            return RealMarketSupplier(coroutineDispatcher, store, marketDispatcher, marketActionFactory)
        }
    }
}
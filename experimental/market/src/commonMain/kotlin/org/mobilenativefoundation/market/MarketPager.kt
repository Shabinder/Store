package org.mobilenativefoundation.market

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.mobilenativefoundation.storex.paging.Identifiable
import org.mobilenativefoundation.storex.paging.Pager
import org.mobilenativefoundation.storex.paging.PagerBuilder
import org.mobilenativefoundation.storex.paging.StoreX


interface MarketPager<Id : Comparable<Id>> {

    fun contribute(anchorPosition: Flow<Id>)

    companion object {
        fun <Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any, A : Market.Action, D : Market.Dispatcher<A>> from(
            coroutineDispatcher: CoroutineDispatcher,
            marketDispatcher: D,
            actionFactory: (state: StoreX.Paging.State<Id, K, V, E>) -> A,
            pagerBuilder: PagerBuilder<Id, K, V, E>.() -> Pager<Id, K, V, E>
        ): MarketPager<Id> {
            return RealMarketPager(
                coroutineDispatcher, pagerBuilder(PagerBuilder(coroutineDispatcher)), marketDispatcher, actionFactory
            )
        }
    }
}

class RealMarketPager<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any, A : Market.Action, D : Market.Dispatcher<A>>(
    coroutineDispatcher: CoroutineDispatcher,
    private val pager: Pager<Id, K, V, E>,
    private val marketDispatcher: D,
    private val actionFactory: (state: StoreX.Paging.State<Id, K, V, E>) -> A
) : MarketPager<Id> {

    private val coroutineScope = CoroutineScope(coroutineDispatcher)

    override fun contribute(anchorPosition: Flow<Id>) {
        coroutineScope.launch {

            pager(anchorPosition)
            pager.flow.collect {
                val action = actionFactory(it)
                marketDispatcher.dispatch(action)
            }
        }
    }

}
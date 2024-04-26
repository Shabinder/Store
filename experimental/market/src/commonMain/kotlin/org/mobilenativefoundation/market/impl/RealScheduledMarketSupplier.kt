package org.mobilenativefoundation.market.impl

import kotlinx.coroutines.*
import org.mobilenativefoundation.market.Market
import org.mobilenativefoundation.market.ScheduledMarketSupplier
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.impl.extensions.fresh
import kotlin.time.Duration

class RealScheduledMarketSupplier<K : Any, O : Any, A : Market.Action, D : Market.Dispatcher<A>>(
    coroutineDispatcher: CoroutineDispatcher,
    private val store: Store<K, O>,
    private val marketDispatcher: D,
    private val marketActionFactory: MarketActionFactory<O, A>,
) : ScheduledMarketSupplier<K> {

    private val coroutineScope = CoroutineScope(coroutineDispatcher)
    private val jobs = mutableMapOf<K, SupplyInfo>()

    override fun supply(key: K, duration: Duration, initialDelay: Duration): () -> Unit {
        val supplyInfo = jobs.getOrPut(key) {
            val job = coroutineScope.launch {
                delay(initialDelay)

                while (true) {
                    supplyMarket(key)
                    delay(duration)
                }
            }
            SupplyInfo(duration, job, 1)
        }

        updateSupplyInfo(key, supplyInfo, duration)

        return { unsubscribe(key) }
    }

    private fun updateSupplyInfo(key: K, supplyInfo: SupplyInfo, duration: Duration) {
        val (oldDuration, job, subscribers) = supplyInfo
        val newDuration = minOf(oldDuration, duration)
        val newSubscribers = subscribers + 1

        jobs[key] = SupplyInfo(newDuration, job, newSubscribers)
    }

    private suspend fun supplyMarket(key: K) {
        try {
            val storeOutput = store.fresh(key)
            val marketAction = marketActionFactory.create(storeOutput)
            marketDispatcher.dispatch(marketAction)
        } catch (e: Exception) {
            // TODO
        }
    }

    private fun unsubscribe(key: K) {
        val supplyInfo = jobs[key] ?: return
        val (_, _, subscribers) = supplyInfo
        val newSubscribers = maxOf(subscribers - 1, 0)

        if (newSubscribers == 0) {
            supplyInfo.job.cancel()
            jobs.remove(key)
        } else {
            jobs[key] = SupplyInfo(supplyInfo.duration, supplyInfo.job, newSubscribers)
        }
    }

    private data class SupplyInfo(val duration: Duration, val job: Job, val subscribers: Int)
}
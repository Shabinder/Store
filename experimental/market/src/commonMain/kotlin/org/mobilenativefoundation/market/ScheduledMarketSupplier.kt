package org.mobilenativefoundation.market

import kotlin.time.Duration

interface ScheduledMarketSupplier<K : Any> {
    fun supply(key: K, duration: Duration, initialDelay: Duration = Duration.ZERO): () -> Unit
}



package org.mobilenativefoundation.market

interface MarketSupplier<K : Any> {
    fun supply(key: K)
}
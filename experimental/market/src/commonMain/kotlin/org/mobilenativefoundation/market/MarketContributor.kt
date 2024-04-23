package org.mobilenativefoundation.market

interface MarketContributor<K : Any> {
    fun contribute(key: K)
}
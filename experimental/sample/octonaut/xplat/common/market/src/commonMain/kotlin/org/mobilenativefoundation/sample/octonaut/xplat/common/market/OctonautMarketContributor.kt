package org.mobilenativefoundation.sample.octonaut.xplat.common.market

import org.mobilenativefoundation.market.impl.RealMarketContributor

typealias OctonautMarketContributor<K, O> = RealMarketContributor<K, O, OctonautMarketAction, OctonautMarketDispatcher>
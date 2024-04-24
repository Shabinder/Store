package org.mobilenativefoundation.sample.octonaut.xplat.common.market

import org.mobilenativefoundation.market.impl.RealMarketSupplier

typealias OctonautMarketContributor<K, O> = RealMarketSupplier<K, O, OctonautMarketAction, OctonautMarketDispatcher>
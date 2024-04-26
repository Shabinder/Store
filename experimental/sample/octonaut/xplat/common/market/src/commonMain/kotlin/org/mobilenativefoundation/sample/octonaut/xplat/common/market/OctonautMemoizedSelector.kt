package org.mobilenativefoundation.sample.octonaut.xplat.common.market

import org.mobilenativefoundation.market.memoize

fun <P : Any, R : Any> memoize(
    selectState: (OctonautMarketState) -> R,
    getParams: (OctonautMarketState) -> P,
    areParamsEqual: (P, P) -> Boolean = { p1, p2 -> p1 == p2 }
) = memoize(
    selectState = selectState,
    getParams = getParams,
    areParamsEqual = areParamsEqual
)
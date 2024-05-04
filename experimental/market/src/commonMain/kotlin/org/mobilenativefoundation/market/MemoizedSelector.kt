package org.mobilenativefoundation.market


private class MemoizedSelector<S : Market.State, P : Any, R : Any>(
    private val selector: (S) -> R,
    private val getParams: (S) -> P,
    private val areParamsEqual: (P, P) -> Boolean = { p1, p2 -> p1 == p2 },
) : Market.Selector<S, R> {

    private var lastParams: P? = null
    private var lastResult: R? = null

    override fun select(state: S): R {
        val params = getParams(state)

        if (lastParams == null || params == Unit || !areParamsEqual(params, lastParams!!)) {
            lastParams = params
            lastResult = selector(state)
        }

        return lastResult!!
    }
}

fun <S : Market.State, P : Any, R : Any> memoize(
    selectState: (S) -> R,
    getParams: (S) -> P,
    areParamsEqual: (P, P) -> Boolean = { p1, p2 -> p1 == p2 }
): Market.Selector<S, R> {
    return MemoizedSelector(selectState, getParams, areParamsEqual)
}
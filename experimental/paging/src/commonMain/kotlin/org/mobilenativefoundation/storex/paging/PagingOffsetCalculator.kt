package org.mobilenativefoundation.storex.paging

fun interface PagingOffsetCalculator<Id: Comparable<Id>, K: Any> {
    fun calculate(key: K): Id
}
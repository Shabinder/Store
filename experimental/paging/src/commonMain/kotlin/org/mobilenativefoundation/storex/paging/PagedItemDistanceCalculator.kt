package org.mobilenativefoundation.storex.paging

fun interface PagedItemDistanceCalculator<Id : Comparable<Id>> {
    fun calculate(a: Id, b: Id): Int
}
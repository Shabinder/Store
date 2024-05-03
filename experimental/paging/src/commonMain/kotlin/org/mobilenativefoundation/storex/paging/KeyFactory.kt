package org.mobilenativefoundation.storex.paging

fun interface KeyFactory<Id : Comparable<Id>, K : Any> {
    fun create(anchorPosition: Id): K
}
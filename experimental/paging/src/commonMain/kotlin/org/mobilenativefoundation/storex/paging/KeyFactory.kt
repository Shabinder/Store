package org.mobilenativefoundation.storex.paging

fun interface KeyFactory<Id : Comparable<Id>, K : StoreXPaging.Key> {
    fun create(anchorPosition: Id): K
}
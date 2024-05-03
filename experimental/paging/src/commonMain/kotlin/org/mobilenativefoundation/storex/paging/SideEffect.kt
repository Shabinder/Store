package org.mobilenativefoundation.storex.paging

fun interface SideEffect<Id : Comparable<Id>, K : StoreXPaging.Key, V : Identifiable<Id>, E : StoreXPaging.Error> {
    operator fun invoke(state: StoreXPaging.State<Id, K, V, E>)
}
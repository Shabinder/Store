package org.mobilenativefoundation.storex.paging

fun interface SideEffect<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, E : Any> {
    operator fun invoke(state: StoreX.Paging.State<Id, K, V, E>)
}
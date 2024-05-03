package org.mobilenativefoundation.storex.paging

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


interface Pager<Id : Comparable<Id>,  K : Any,  V : Identifiable<Id>,  E : Any> {

    val flow: Flow<StoreX.Paging.State<Id, K, V, E>>

    val state: StateFlow<StoreX.Paging.State<Id, K, V, E>>

    val pagingItems: Flow<StoreX.Paging.Items<Id, V>>

    operator fun invoke(anchorPosition: Flow<Id>)
}
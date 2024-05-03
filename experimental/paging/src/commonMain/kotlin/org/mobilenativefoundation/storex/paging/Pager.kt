package org.mobilenativefoundation.storex.paging

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


interface Pager<Id : Comparable<Id>,  K : StoreXPaging.Key,  V : Identifiable<Id>,  E : StoreXPaging.Error> {

    val flow: Flow<StoreXPaging.State<Id, K, V, E>>

    val state: StateFlow<StoreXPaging.State<Id, K, V, E>>

    val pagingItems: Flow<StoreXPaging.Items<Id, V>>

    operator fun invoke(anchorPosition: Flow<Id>)
}
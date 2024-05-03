package org.mobilenativefoundation.storex.paging

interface Loader<Id : Comparable<Id>,  K : StoreXPaging.Key,  V : Identifiable<Id>,  E : StoreXPaging.Error> {
    suspend operator fun invoke(anchorPosition: Id)
}
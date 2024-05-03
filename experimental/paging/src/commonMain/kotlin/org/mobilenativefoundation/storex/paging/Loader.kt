package org.mobilenativefoundation.storex.paging

interface Loader<Id : Comparable<Id>,  K : Any,  V : Identifiable<Id>,  E : Any> {
    suspend operator fun invoke(anchorPosition: Id)
}
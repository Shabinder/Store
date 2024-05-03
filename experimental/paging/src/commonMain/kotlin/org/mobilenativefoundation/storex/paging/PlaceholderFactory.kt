package org.mobilenativefoundation.storex.paging

fun interface PlaceholderFactory<Id : Comparable<Id>, V : Identifiable<Id>> {
    operator fun invoke(): StoreXPaging.Data.Item<Id, V>
}
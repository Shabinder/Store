package org.mobilenativefoundation.storex.paging

interface Identifiable<Id : Comparable<Id>> {
    val id: Id
}
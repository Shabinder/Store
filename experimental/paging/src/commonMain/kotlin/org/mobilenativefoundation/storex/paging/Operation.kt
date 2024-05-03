package org.mobilenativefoundation.storex.paging

sealed interface Operation<Id : Comparable<Id>, K : Any, V : Identifiable<Id>> {

    val shouldApply: (key: K?) -> Boolean

    data class Sort<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, S : Any>(
        val strategy: SortingStrategy<Id, V, S>,
        val params: S,
        override val shouldApply: (key: K?) -> Boolean
    ) : Operation<Id, K, V>

    data class Filter<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, F : Any>(
        val strategy: FilteringStrategy<Id, V, F>,
        val params: F,
        override val shouldApply: (key: K?) -> Boolean
    ) : Operation<Id, K, V>

    data class Group<Id : Comparable<Id>, K : Any, V : Identifiable<Id>, G : Any>(
        val strategy: GroupingStrategy<Id, V, G>,
        val params: G,
        override val shouldApply: (key: K?) -> Boolean
    ) : Operation<Id, K, V>

    data class Transform<Id : Comparable<Id>, K : Any, V : Identifiable<Id>>(
        val strategy: TransformationStrategy<Id, V>,
        override val shouldApply: (key: K?) -> Boolean,
    ) : Operation<Id, K, V>

    data class Deduplicate<Id : Comparable<Id>, K : Any, V : Identifiable<Id>>(
        override val shouldApply: (key: K?) -> Boolean,
        val strategy: DeduplicationStrategy<Id, V>
    ) : Operation<Id, K, V>

    data class Validate<Id : Comparable<Id>, K : Any, V : Identifiable<Id>>(
        override val shouldApply: (key: K?) -> Boolean,
        val strategy: ValidationStrategy<Id, V>
    ) : Operation<Id, K, V>

}



package org.mobilenativefoundation.storex.paging

/**
 * Represents different insertion strategies for adding new data to the paging buffer.
 */
enum class InsertionStrategy {
    /**
     * Appends new data to the end of the buffer.
     */
    APPEND,

    /**
     * Prepends new data to the beginning of the buffer.
     */
    PREPEND,

    /**
     * Replaces the existing data in the buffer with the new data.
     */
    REPLACE,
}
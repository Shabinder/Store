package org.mobilenativefoundation.market.repository

enum class ConflictResolutionStrategy {
    DESTRUCTIVE, // Overwrite local changes that haven't synced with the network
    DEFER, // Before pulling, push local changes that haven't synced with the network
}
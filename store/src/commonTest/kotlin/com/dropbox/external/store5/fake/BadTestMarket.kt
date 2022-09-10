@file:OptIn(ExperimentalCoroutinesApi::class)

package com.dropbox.external.store5.fake

import com.dropbox.external.store5.ConflictResolver
import com.dropbox.external.store5.Store
import com.dropbox.external.store5.fake.model.Note
import com.dropbox.external.store5.impl.MemoryLruCache
import com.dropbox.external.store5.impl.RealMarket
import kotlinx.coroutines.ExperimentalCoroutinesApi

internal object BadTestMarket {
    val memoryLruCache = MemoryLruCache(10)
    val db = FakeDb()

    private val memoryLruCacheStore = Store.by<String, Note, Note>(
        read = { throw Exception() },
        write = { _, _ -> throw Exception() },
        delete = { throw Exception() },
        deleteAll = { throw Exception() }
    )

    private val dbStore = Store.by<String, Note, Note>(
        read = { throw Exception() },
        write = { _, _ -> throw Exception() },
        delete = { throw Exception() },
        deleteAll = { throw Exception() },
    )

    private val conflictResolver = ConflictResolver<String, Note, Note>(
        setLastFailedWriteTime = { key, updated -> db.setLastWriteTime(key, updated) },
        getLastFailedWriteTime = { key -> db.getLastWriteTime(key) },
        deleteFailedWriteRecord = { key -> db.deleteWriteRequest(key) }
    )

    internal fun build() = RealMarket(
        stores = listOf(memoryLruCacheStore, dbStore),
        conflictResolver = conflictResolver
    )
}




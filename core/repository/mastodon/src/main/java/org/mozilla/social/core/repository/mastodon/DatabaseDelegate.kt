package org.mozilla.social.core.repository.mastodon

import androidx.room.withTransaction
import androidx.sqlite.db.SimpleSQLiteQuery
import org.mozilla.social.core.database.SocialDatabase

class DatabaseDelegate(private val socialDatabase: SocialDatabase) {
    suspend fun <R> withTransaction(block: suspend () -> R): R = socialDatabase.withTransaction(block)

    fun clearAllTables() = socialDatabase.clearAllTables()

    fun vacuum() = socialDatabase.query(SimpleSQLiteQuery("VACUUM"))
}

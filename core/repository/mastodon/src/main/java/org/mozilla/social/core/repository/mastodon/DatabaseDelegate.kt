package org.mozilla.social.core.repository.mastodon

import androidx.room.RoomDatabase
import androidx.room.withTransaction
import org.mozilla.social.core.database.SocialDatabase

class DatabaseDelegate(private val socialDatabase: SocialDatabase) {
    suspend fun <R> withTransaction(block: suspend () -> R): R =
        socialDatabase.withTransaction(block)
}
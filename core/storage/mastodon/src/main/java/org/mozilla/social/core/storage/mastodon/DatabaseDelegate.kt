package org.mozilla.social.core.storage.mastodon

import androidx.room.RoomDatabase
import androidx.room.withTransaction
import org.mozilla.social.core.database.SocialDatabase

class DatabaseDelegate(private val socialDatabase: SocialDatabase) {
    public suspend fun <R> withTransaction(block: suspend () -> R): R {
        return socialDatabase.withTransaction(block)
    }
}
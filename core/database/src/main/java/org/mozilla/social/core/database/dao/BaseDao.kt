package org.mozilla.social.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert

@Dao
interface BaseDao<T> {
    @Upsert
    suspend fun upsert(t: T)

    @Upsert
    suspend fun upsertAll(t: List<T>)

    @Delete
    suspend fun delete(t: T)
}

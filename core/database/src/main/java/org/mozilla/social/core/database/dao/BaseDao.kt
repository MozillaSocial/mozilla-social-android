package org.mozilla.social.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert

@Dao
interface BaseDao<T> {
    @Upsert
    fun upsert(t: T)

    @Upsert
    fun upsertAll(t: List<T>)

    @Delete
    fun delete(t: T)
}

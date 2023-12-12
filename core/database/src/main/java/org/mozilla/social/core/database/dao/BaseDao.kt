package org.mozilla.social.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface BaseDao<T> {
    @Update
    fun update(t: T): Int

    @Upsert
    fun insert(t: T)

    @Upsert
    fun insertAll(t: List<T>)

    @Delete
    fun delete(t: T)
}

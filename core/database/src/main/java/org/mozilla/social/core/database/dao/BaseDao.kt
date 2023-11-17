package org.mozilla.social.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(t: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg t: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(t: List<T>)

    @Update
    fun update(t: T)

    @Delete
    fun delete(t: T)
}

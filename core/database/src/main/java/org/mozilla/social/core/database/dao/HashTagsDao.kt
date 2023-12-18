package org.mozilla.social.core.database.dao

import androidx.room.Dao
import org.mozilla.social.core.database.model.entities.DatabaseHashTagEntity

@Dao
interface HashTagsDao : BaseDao<DatabaseHashTagEntity>
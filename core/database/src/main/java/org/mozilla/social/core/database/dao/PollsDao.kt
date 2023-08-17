package org.mozilla.social.core.database.dao

import androidx.room.Dao
import org.mozilla.social.core.database.model.DatabasePoll

@Dao
interface PollsDao : BaseDao<DatabasePoll>
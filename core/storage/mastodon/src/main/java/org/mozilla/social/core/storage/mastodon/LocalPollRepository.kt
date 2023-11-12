package org.mozilla.social.core.storage.mastodon

import org.mozilla.social.core.database.dao.PollsDao
import org.mozilla.social.core.model.Poll
import org.mozilla.social.core.storage.mastodon.status.toDatabaseModel

class LocalPollRepository(private val pollsDao: PollsDao) {

    fun insertPolls(polls: List<Poll>) {
        pollsDao.insertAll(polls.map { it.toDatabaseModel() })
    }
}
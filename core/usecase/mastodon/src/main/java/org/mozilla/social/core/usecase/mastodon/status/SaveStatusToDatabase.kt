package org.mozilla.social.core.usecase.mastodon.status

import androidx.room.withTransaction
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.repository.mastodon.PollRepository
import org.mozilla.social.core.repository.mastodon.StatusRepository
import org.mozilla.social.core.repository.mastodon.model.status.toDatabaseModel

internal class SaveStatusToDatabase internal constructor(
    private val socialDatabase: SocialDatabase,
    private val statusRepository: StatusRepository,
    private val pollRepository: PollRepository,
) {
    suspend operator fun invoke(vararg statuses: Status) {
        socialDatabase.withTransaction {
            val boostedStatuses = statuses.mapNotNull { it.boostedStatus }
            pollRepository.insertAll(
                boostedStatuses.mapNotNull {
                    it.poll
                },
            )
            socialDatabase.accountsDao().insertAll(
                boostedStatuses.map {
                    it.account.toDatabaseModel()
                },
            )
            statusRepository.insertAll(boostedStatuses)

            pollRepository.insertAll(
                statuses.mapNotNull {
                    it.poll
                },
            )
            socialDatabase.accountsDao().insertAll(
                statuses.map {
                    it.account.toDatabaseModel()
                },
            )
            statusRepository.insertAll(statuses.asList())
        }
    }

    suspend operator fun invoke(statuses: List<Status>) {
        invoke(*statuses.toTypedArray())
    }
}

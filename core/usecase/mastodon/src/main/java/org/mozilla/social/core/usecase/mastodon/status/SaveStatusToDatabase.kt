package org.mozilla.social.core.usecase.mastodon.status

import androidx.room.withTransaction
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.PollRepository
import org.mozilla.social.core.repository.mastodon.StatusRepository
import org.mozilla.social.core.repository.mastodon.model.status.toDatabaseModel

internal class SaveStatusToDatabase internal constructor(
    private val socialDatabase: SocialDatabase,
    private val statusRepository: StatusRepository,
    private val accountRepository: AccountRepository,
    private val pollRepository: PollRepository,
) {
    suspend operator fun invoke(vararg statuses: Status) {
        socialDatabase.withTransaction {
            val boostedStatuses = statuses.mapNotNull { it.boostedStatus }

            pollRepository.insertAll(boostedStatuses.mapNotNull { it.poll })
            accountRepository.insertAll(boostedStatuses.map { it.account })
            statusRepository.insertAll(boostedStatuses)

            pollRepository.insertAll(statuses.mapNotNull { it.poll })
            accountRepository.insertAll(statuses.map { it.account })
            statusRepository.insertAll(statuses.asList())
        }
    }

    suspend operator fun invoke(statuses: List<Status>) {
        invoke(*statuses.toTypedArray())
    }
}

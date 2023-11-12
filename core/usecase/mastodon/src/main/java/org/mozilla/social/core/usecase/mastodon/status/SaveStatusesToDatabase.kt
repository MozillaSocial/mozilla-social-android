package org.mozilla.social.core.usecase.mastodon.status

import org.mozilla.social.core.model.Status
import org.mozilla.social.core.storage.mastodon.DatabaseDelegate
import org.mozilla.social.core.storage.mastodon.LocalAccountRepository
import org.mozilla.social.core.storage.mastodon.LocalPollRepository
import org.mozilla.social.core.storage.mastodon.LocalStatusRepository

class SaveStatusesToDatabase(
    private val localStatusRepository: LocalStatusRepository,
    private val localPollRepository: LocalPollRepository,
    private val localAccountsRepository: LocalAccountRepository,
    private val databaseDelegate: DatabaseDelegate,
) {
    suspend operator fun invoke(statuses: List<Status>) {
        invoke(*statuses.toTypedArray())
    }
    suspend operator fun invoke(vararg statuses: Status) {
        databaseDelegate.withTransaction {
            val boostedStatuses = statuses.mapNotNull { it.boostedStatus }
            localPollRepository.insertPolls(boostedStatuses.mapNotNull { it.poll })
            localAccountsRepository.insertAccounts(boostedStatuses.map { it.account })
            localStatusRepository.insertStatuses(boostedStatuses)
            localPollRepository.insertPolls(statuses.mapNotNull { it.poll })
            localAccountsRepository.insertAccounts(statuses.map { it.account })
            localStatusRepository.insertStatuses(statuses.toList())
        }
    }
}
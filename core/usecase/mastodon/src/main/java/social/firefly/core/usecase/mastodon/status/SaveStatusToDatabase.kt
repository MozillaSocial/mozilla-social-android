package social.firefly.core.usecase.mastodon.status

import social.firefly.common.annotations.PreferUseCase
import social.firefly.core.model.Status
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.PollRepository
import social.firefly.core.repository.mastodon.StatusRepository

class SaveStatusToDatabase internal constructor(
    private val databaseDelegate: DatabaseDelegate,
    private val statusRepository: StatusRepository,
    private val accountRepository: AccountRepository,
    private val pollRepository: PollRepository,
) {
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(vararg statuses: Status) {
        databaseDelegate.withTransaction {
            val boostedStatuses = statuses.mapNotNull { it.boostedStatus }

            pollRepository.insertAll(boostedStatuses.mapNotNull { it.poll })
            pollRepository.insertAll(statuses.mapNotNull { it.poll })

            accountRepository.insertAll(boostedStatuses.map { it.account })
            accountRepository.insertAll(statuses.map { it.account })

            statusRepository.insertAll(boostedStatuses)
            statusRepository.insertAll(statuses.asList())
        }
    }

    suspend operator fun invoke(statuses: List<Status>) {
        invoke(*statuses.toTypedArray())
    }
}

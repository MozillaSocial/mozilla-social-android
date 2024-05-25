package social.firefly.core.usecase.mastodon.status

import kotlinx.coroutines.coroutineScope
import social.firefly.core.model.Status
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.repository.mastodon.AccountRepository
import timber.log.Timber

class GetInReplyToAccountNames internal constructor(
    private val accountRepository: AccountRepository,
) {

    suspend operator fun invoke(
        statuses: List<Status>
    ): List<Status> =
        coroutineScope {
            val accounts = try {
                accountRepository.getAccounts(statuses.mapNotNull { it.inReplyToAccountId })
            } catch (e: Exception) {
                Timber.e(e)
                return@coroutineScope statuses
            }
            statuses.map { status ->
                status.copy(
                    inReplyToAccountName = accounts.find {
                        status.inReplyToAccountId == it.accountId
                    }?.displayName,
                )
            }
        }

    suspend operator fun invoke(
        mastodonPagedResponse: MastodonPagedResponse<Status>
    ): MastodonPagedResponse<Status> {
        val newStatuses = invoke(mastodonPagedResponse.items)
        return MastodonPagedResponse(
            items = newStatuses,
            pagingLinks = mastodonPagedResponse.pagingLinks,
        )
    }
}
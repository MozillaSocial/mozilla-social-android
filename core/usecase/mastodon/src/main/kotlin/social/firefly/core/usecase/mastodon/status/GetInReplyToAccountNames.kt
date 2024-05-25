package social.firefly.core.usecase.mastodon.status

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import social.firefly.core.model.Status
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.model.exceptions.AccountNotFoundException
import timber.log.Timber

class GetInReplyToAccountNames internal constructor(
    private val accountRepository: AccountRepository,
) {

    suspend operator fun invoke(
        statuses: List<Status>
    ): List<Status> =
        coroutineScope {
            statuses.map { status ->
                async {
                    try {
                        status.copy(
                            inReplyToAccountName = status.inReplyToAccountId?.let { accountId ->
                                accountRepository.getAccount(accountId).displayName
                            },
                        )
                    } catch (e: AccountNotFoundException) {
                        Timber.e(e)
                        return@async status
                    }
                }
            }.map {
                it.await()
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
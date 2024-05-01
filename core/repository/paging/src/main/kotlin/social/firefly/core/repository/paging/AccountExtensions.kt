package social.firefly.core.repository.paging

import social.firefly.core.model.Account
import social.firefly.core.model.Relationship
import social.firefly.core.repository.mastodon.AccountRepository

suspend fun List<Account>.getRelationships(accountRepository: AccountRepository): List<Relationship> =
    accountRepository.getAccountRelationships(map { it.accountId })

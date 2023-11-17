package org.mozilla.social.core.usecase.mastodon.remotemediators

import org.mozilla.social.core.model.Account
import org.mozilla.social.core.model.Relationship
import org.mozilla.social.core.repository.mastodon.AccountRepository

suspend fun List<Account>.getRelationships(accountRepository: AccountRepository): List<Relationship> =
    accountRepository.getAccountRelationships(map { it.accountId })

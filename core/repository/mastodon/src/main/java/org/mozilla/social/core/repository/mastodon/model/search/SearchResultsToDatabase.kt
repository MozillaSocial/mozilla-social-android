package org.mozilla.social.core.repository.mastodon.model.search

import org.mozilla.social.core.database.model.entities.accountCollections.SearchedAccount
import org.mozilla.social.core.database.model.entities.hashtagCollections.SearchedHashTag
import org.mozilla.social.core.database.model.entities.statusCollections.SearchedStatus
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.model.HashTag
import org.mozilla.social.core.model.Status

fun List<Account>.toSearchedAccount(): List<SearchedAccount> = mapIndexed { index, account ->
    SearchedAccount(
        accountId = account.accountId,
        position = index,
    )
}

fun List<Status>.toSearchedStatus(): List<SearchedStatus> = mapIndexed { index, status ->
    SearchedStatus(
        statusId = status.statusId,
        position = index,
        accountId = status.account.accountId,
        pollId = status.poll?.pollId,
        boostedStatusId = status.boostedStatus?.statusId,
        boostedStatusAccountId = status.boostedStatus?.account?.accountId,
        boostedPollId = status.boostedStatus?.poll?.pollId,
    )
}

fun List<HashTag>.toSearchedHashTags(): List<SearchedHashTag> = mapIndexed { index, hashTag ->
    SearchedHashTag(
        hashTagName = hashTag.name,
        position = index,
    )
}
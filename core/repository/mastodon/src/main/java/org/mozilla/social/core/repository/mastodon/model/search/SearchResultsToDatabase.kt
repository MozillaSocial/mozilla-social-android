package org.mozilla.social.core.repository.mastodon.model.search

import org.mozilla.social.core.database.model.entities.accountCollections.SearchedAccount
import org.mozilla.social.core.database.model.entities.hashtagCollections.SearchedHashTag
import org.mozilla.social.core.database.model.entities.statusCollections.SearchedStatus
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.model.HashTag
import org.mozilla.social.core.model.Status

fun List<Account>.toSearchedAccount(
    startIndex: Int = 0,
): List<SearchedAccount> = mapIndexed { index, account ->
    SearchedAccount(
        accountId = account.accountId,
        position = startIndex + index,
    )
}

fun List<Status>.toSearchedStatus(
    startIndex: Int = 0,
): List<SearchedStatus> = mapIndexed { index, status ->
    SearchedStatus(
        statusId = status.statusId,
        position = startIndex + index,
    )
}

fun List<HashTag>.toSearchedHashTags(
    startIndex: Int = 0,
): List<SearchedHashTag> = mapIndexed { index, hashTag ->
    SearchedHashTag(
        hashTagName = hashTag.name,
        position = startIndex + index,
    )
}
package social.firefly.core.repository.mastodon.model.search

import social.firefly.core.database.model.entities.accountCollections.SearchedAccount
import social.firefly.core.database.model.entities.hashtagCollections.SearchedHashTag
import social.firefly.core.database.model.entities.statusCollections.SearchedStatus
import social.firefly.core.model.Account
import social.firefly.core.model.HashTag
import social.firefly.core.model.Status

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
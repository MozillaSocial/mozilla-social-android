package org.mozilla.social.core.usecase.mastodon.status

import androidx.room.withTransaction
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.model.DatabaseStatus
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.repository.mastodon.PollRepository
import org.mozilla.social.core.repository.mastodon.model.status.toDatabaseModel

class SaveStatusToDatabase internal constructor(
    private val socialDatabase: SocialDatabase,
    private val pollRepository: PollRepository,
) {
    suspend operator fun invoke(vararg statuses: Status) {
        socialDatabase.withTransaction {
            val boostedStatuses = statuses.mapNotNull { it.boostedStatus }
            pollRepository.insertAll(boostedStatuses.mapNotNull {
                it.poll
            })
            socialDatabase.accountsDao().insertAll(boostedStatuses.map {
                it.account.toDatabaseModel()
            })
            socialDatabase.statusDao().insertAll(boostedStatuses.map {
                it.toDatabaseModel()
            })

            pollRepository.insertAll(statuses.mapNotNull {
                it.poll
            })
            socialDatabase.accountsDao().insertAll(statuses.map {
                it.account.toDatabaseModel()
            })
            socialDatabase.statusDao().insertAll(statuses.map {
                it.toDatabaseModel()
            })
        }
    }

    suspend operator fun invoke(statuses: List<Status>) {
        invoke(*statuses.toTypedArray())
    }
}

private fun Status.toDatabaseModel(): DatabaseStatus =
    DatabaseStatus(
        statusId = statusId,
        uri = uri,
        createdAt = createdAt,
        accountId = account.accountId,
        content = content,
        visibility = visibility.toDatabaseModel(),
        isSensitive = isSensitive,
        contentWarningText = contentWarningText,
        mediaAttachments = mediaAttachments.map { it.toDatabaseModel() },
        mentions = mentions.map { it.toDatabaseModel() },
        hashTags = hashTags.map { it.toDatabaseModel() },
        emojis = emojis.map { it.toDatabaseModel() },
        boostsCount = boostsCount,
        favouritesCount = favouritesCount,
        repliesCount = repliesCount,
        application = application?.toDatabaseModel(),
        url = url,
        inReplyToId = inReplyToId,
        inReplyToAccountId = inReplyToAccountId,
        inReplyToAccountName = inReplyToAccountName,
        boostedStatusId = boostedStatus?.statusId,
        boostedStatusAccountId = boostedStatus?.account?.accountId,
        pollId = poll?.pollId,
        card = card?.toDatabaseModel(),
        language = language,
        plainText = plainText,
        isFavorited = isFavourited,
        isBoosted = isBoosted,
        isMuted = isMuted,
        isBookmarked = isBookmarked,
        isPinned = isPinned,
        isBeingDeleted = isBeingDeleted,
    )

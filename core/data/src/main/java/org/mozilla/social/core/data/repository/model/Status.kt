package org.mozilla.social.core.data.repository.model

import org.mozilla.social.core.network.model.NetworkAccount
import org.mozilla.social.core.network.model.NetworkAttachment
import org.mozilla.social.core.network.model.NetworkStatus
import org.mozilla.social.core.network.model.NetworkStatusVisibility
import org.mozilla.social.model.Account
import org.mozilla.social.model.Attachment
import org.mozilla.social.model.Status
import org.mozilla.social.model.StatusVisibility

fun NetworkStatus.toExternalModel(
    inReplyToAccountName: String? = null
): Status =
    Status(
        statusId = statusId,
        uri = uri,
        createdAt = createdAt,
        account = account.toExternalModel(),
        content = content,
        visibility = visibility.toExternalModel(),
        isSensitive = isSensitive,
        contentWarningText = contentWarningText,
        mediaAttachments = mediaAttachments.map { it.toExternalModel() },
        mentions = mentions,
        hashTags = hashTags,
        emojis = emojis,
        boostsCount = boostsCount,
        favouritesCount = favouritesCount,
        repliesCount = repliesCount,
        application = application,
        url = url,
        inReplyToId = inReplyToId,
        inReplyToAccountId = inReplyToAccountId,
        boostedStatus = boostedStatus,
        poll = poll,
        card = card,
        language = language,
        plainText = plainText,
        isFavourited = isFavourited,
        isBoosted = isBoosted,
        isMuted = isMuted,
        isBookmarked = isBookmarked,
        isPinned = isPinned,
        inReplyToAccountName = inReplyToAccountName,
    )

fun NetworkAccount.toExternalModel() = Account(
    accountId = accountId,
    username = username,
    acct = acct,
    url = url,
    displayName = displayName,
    bio = bio,
    avatarUrl = avatarUrl,
    avatarStaticUrl = avatarStaticUrl,
    headerUrl = headerUrl,
    headerStaticUrl = headerStaticUrl,
    isLocked = isLocked,
    emojis = emojis,
    createdAt = createdAt,
    lastStatusAt = lastStatusAt,
    statusesCount = statusesCount,
    followersCount = followersCount,
    followingCount = followingCount,
    isDiscoverable = isDiscoverable,
    movedTo = movedTo,
    isGroup = isGroup,
    fields = fields,
    isBot = isBot,
    source = source,
    isSuspended = isSuspended,
    muteExpiresAt = muteExpiresAt,
)

fun NetworkStatusVisibility.toExternalModel(): StatusVisibility =
    when(this) {
        NetworkStatusVisibility.Direct -> StatusVisibility.Direct
        NetworkStatusVisibility.Private -> StatusVisibility.Private
        NetworkStatusVisibility.Public -> StatusVisibility.Public
        NetworkStatusVisibility.Unlisted -> StatusVisibility.Unlisted
    }

fun NetworkAttachment.toExternalModel(): Attachment =
    when (this) {
        is NetworkAttachment.Image -> Attachment.Image(
            attachmentId = attachmentId,
            url = url,
            previewUrl = previewUrl,
            remoteUrl = remoteUrl,
            previewRemoteUrl = previewRemoteUrl,
            textUrl = textUrl,
            description = description,
            blurHash = blurHash
        )
        is NetworkAttachment.Gifv -> Attachment.Gifv(
            attachmentId = attachmentId,
            url = url,
            previewUrl = previewUrl,
            remoteUrl = remoteUrl,
            previewRemoteUrl = previewRemoteUrl,
            textUrl = textUrl,
            description = description
        )
        is NetworkAttachment.Video -> Attachment.Video(
            attachmentId = attachmentId,
            url = url,
            previewUrl = previewUrl,
            remoteUrl = remoteUrl,
            previewRemoteUrl = previewRemoteUrl,
            textUrl = textUrl,
            description = description,
            blurHash = blurHash
        )
        is NetworkAttachment.Audio -> Attachment.Audio(
            attachmentId = attachmentId,
            url = url,
            previewUrl = previewUrl,
            remoteUrl = remoteUrl,
            previewRemoteUrl = previewRemoteUrl,
            textUrl = textUrl,
            description = description,
            blurHash = blurHash
        )
        is NetworkAttachment.Unknown -> Attachment.Unknown(
            attachmentId = attachmentId,
            url = url,
            previewUrl = previewUrl,
            remoteUrl = remoteUrl,
            previewRemoteUrl = previewRemoteUrl,
            textUrl = textUrl,
            description = description,
            blurHash = blurHash
        )
    }

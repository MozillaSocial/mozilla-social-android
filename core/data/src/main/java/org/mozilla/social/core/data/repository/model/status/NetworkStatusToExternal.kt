package org.mozilla.social.core.data.repository.model.status

import org.mozilla.social.core.network.model.NetworkAccount
import org.mozilla.social.core.network.model.NetworkApplication
import org.mozilla.social.core.network.model.NetworkAttachment
import org.mozilla.social.core.network.model.NetworkEmoji
import org.mozilla.social.core.network.model.NetworkField
import org.mozilla.social.core.network.model.NetworkHashTag
import org.mozilla.social.core.network.model.NetworkHistory
import org.mozilla.social.core.network.model.NetworkMention
import org.mozilla.social.core.network.model.NetworkPoll
import org.mozilla.social.core.network.model.NetworkPollOption
import org.mozilla.social.core.network.model.NetworkSource
import org.mozilla.social.core.network.model.NetworkStatus
import org.mozilla.social.core.network.model.NetworkStatusVisibility
import org.mozilla.social.model.Account
import org.mozilla.social.model.Application
import org.mozilla.social.model.Attachment
import org.mozilla.social.model.Emoji
import org.mozilla.social.model.Field
import org.mozilla.social.model.HashTag
import org.mozilla.social.model.History
import org.mozilla.social.model.Mention
import org.mozilla.social.model.Poll
import org.mozilla.social.model.PollOption
import org.mozilla.social.model.Source
import org.mozilla.social.model.Status
import org.mozilla.social.model.StatusVisibility

fun NetworkStatus.toExternalModel(): Status =
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
        mentions = mentions.map { it.toExternalModel() },
        hashTags = hashTags.map { it.toExternalModel() },
        emojis = emojis.map { it.toExternalModel() },
        boostsCount = boostsCount,
        favouritesCount = favouritesCount,
        repliesCount = repliesCount,
        application = application?.toExternalModel(),
        url = url,
        inReplyToId = inReplyToId,
        inReplyToAccountId = inReplyToAccountId,
        boostedStatus = boostedStatus?.toExternalModel(),
        poll = poll?.toExternalModel(),
        //TODO map this if we ever need it
        card = null,
        language = language,
        plainText = plainText,
        isFavourited = isFavourited,
        isBoosted = isBoosted,
        isMuted = isMuted,
        isBookmarked = isBookmarked,
        isPinned = isPinned,
    )

fun NetworkAccount.toExternalModel(): Account = Account(
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
    emojis = emojis.map { it.toExternalModel() },
    createdAt = createdAt,
    lastStatusAt = lastStatusAt,
    statusesCount = statusesCount,
    followersCount = followersCount,
    followingCount = followingCount,
    isDiscoverable = isDiscoverable,
    movedTo = movedTo?.toExternalModel(),
    isGroup = isGroup,
    fields = fields?.map { it.toExternalModel() },
    isBot = isBot,
    source = source?.toExternalModel(),
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

fun NetworkMention.toExternalModel(): Mention =
    Mention(
        accountId = accountId,
        username = username,
        acct = acct,
        url = url
    )

fun NetworkHashTag.toExternalModel(): HashTag =
    HashTag(
        name = name,
        url = url,
        history = history?.map { it.toExternalModel() }
    )

fun NetworkHistory.toExternalModel(): History =
    History(
        day = day,
        usageCount = usageCount,
        accountCount = accountCount
    )

fun NetworkEmoji.toExternalModel(): Emoji =
    Emoji(
        shortCode = shortCode,
        url = url,
        staticUrl = staticUrl,
        isVisibleInPicker = isVisibleInPicker,
        category = category
    )

fun NetworkApplication.toExternalModel(): Application =
    Application(
        name = name,
        website = website,
        vapidKey = vapidKey,
        clientId = clientId,
        clientSecret = clientSecret
    )

fun NetworkPoll.toExternalModel(): Poll =
    Poll(
        pollId = pollId,
        isExpired = isExpired,
        allowsMultipleChoices = allowsMultipleChoices,
        votesCount = votesCount,
        options = options.map { it.toExternalModel() },
        emojis = emojis.map { it.toExternalModel() },
        expiresAt = expiresAt,
        votersCount = votersCount,
        hasVoted = hasVoted,
        ownVotes = ownVotes
    )

fun NetworkPollOption.toExternalModel(): PollOption =
    PollOption(
        title = title,
        votesCount = votesCount
    )

fun NetworkField.toExternalModel(): Field =
    Field(
        name = name,
        value = value,
        verifiedAt = verifiedAt
    )

fun NetworkSource.toExternalModel(): Source =
    Source(
        bio = bio,
        fields = fields.map { it.toExternalModel() },
        defaultPrivacy = defaultPrivacy?.toExternalModel(),
        defaultSensitivity = defaultSensitivity,
        defaultLanguage = defaultLanguage,
        followRequestsCount = followRequestsCount
    )

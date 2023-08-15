package org.mozilla.social.core.data.repository.model.status

import org.mozilla.social.core.database.model.DatabaseAccount
import org.mozilla.social.core.database.model.DatabaseApplication
import org.mozilla.social.core.database.model.DatabaseAttachment
import org.mozilla.social.core.database.model.DatabaseBoostedStatus
import org.mozilla.social.core.database.model.DatabaseEmoji
import org.mozilla.social.core.database.model.DatabaseField
import org.mozilla.social.core.database.model.DatabaseHashTag
import org.mozilla.social.core.database.model.DatabaseHistory
import org.mozilla.social.core.database.model.DatabaseMention
import org.mozilla.social.core.database.model.DatabasePoll
import org.mozilla.social.core.database.model.DatabasePollOption
import org.mozilla.social.core.database.model.DatabaseSource
import org.mozilla.social.core.database.model.DatabaseStatus
import org.mozilla.social.core.database.model.DatabaseStatusVisibility
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

fun DatabaseStatus.toExternalModel(): Status =
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
        inReplyToAccountName = inReplyToAccountName,
        boostedStatus = boostedStatus?.toDatabaseStatus()?.toExternalModel(),
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

fun DatabaseBoostedStatus.toDatabaseStatus(): DatabaseStatus = DatabaseStatus(
    statusId = statusId,
    uri = uri,
    createdAt = createdAt,
    account = account,
    content = content,
    visibility = visibility,
    isSensitive = isSensitive,
    contentWarningText = contentWarningText,
    mediaAttachments = mediaAttachments,
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
    inReplyToAccountName = inReplyToAccountName,
    poll = poll,
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

fun DatabaseAccount.toExternalModel(): Account = Account(
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
    //TODO fix
//    movedTo = movedTo?.toExternalModel(),
    isGroup = isGroup,
    fields = fields?.map { it.toExternalModel() },
    isBot = isBot,
    source = source?.toExternalModel(),
    isSuspended = isSuspended,
    muteExpiresAt = muteExpiresAt,
)

fun DatabaseStatusVisibility.toExternalModel(): StatusVisibility =
    when(this) {
        DatabaseStatusVisibility.Direct -> StatusVisibility.Direct
        DatabaseStatusVisibility.Private -> StatusVisibility.Private
        DatabaseStatusVisibility.Public -> StatusVisibility.Public
        DatabaseStatusVisibility.Unlisted -> StatusVisibility.Unlisted
    }

fun DatabaseAttachment.toExternalModel(): Attachment =
    when (this) {
        is DatabaseAttachment.Image -> Attachment.Image(
            attachmentId = attachmentId,
            url = url,
            previewUrl = previewUrl,
            remoteUrl = remoteUrl,
            previewRemoteUrl = previewRemoteUrl,
            textUrl = textUrl,
            description = description,
            blurHash = blurHash
        )
        is DatabaseAttachment.Gifv -> Attachment.Gifv(
            attachmentId = attachmentId,
            url = url,
            previewUrl = previewUrl,
            remoteUrl = remoteUrl,
            previewRemoteUrl = previewRemoteUrl,
            textUrl = textUrl,
            description = description
        )
        is DatabaseAttachment.Video -> Attachment.Video(
            attachmentId = attachmentId,
            url = url,
            previewUrl = previewUrl,
            remoteUrl = remoteUrl,
            previewRemoteUrl = previewRemoteUrl,
            textUrl = textUrl,
            description = description,
            blurHash = blurHash
        )
        is DatabaseAttachment.Audio -> Attachment.Audio(
            attachmentId = attachmentId,
            url = url,
            previewUrl = previewUrl,
            remoteUrl = remoteUrl,
            previewRemoteUrl = previewRemoteUrl,
            textUrl = textUrl,
            description = description,
            blurHash = blurHash
        )
        is DatabaseAttachment.Unknown -> Attachment.Unknown(
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

fun DatabaseMention.toExternalModel(): Mention =
    Mention(
        accountId = accountId,
        username = username,
        acct = acct,
        url = url
    )

fun DatabaseHashTag.toExternalModel(): HashTag =
    HashTag(
        name = name,
        url = url,
        history = history?.map { it.toExternalModel() }
    )

fun DatabaseHistory.toExternalModel(): History =
    History(
        day = day,
        usageCount = usageCount,
        accountCount = accountCount
    )

fun DatabaseEmoji.toExternalModel(): Emoji =
    Emoji(
        shortCode = shortCode,
        url = url,
        staticUrl = staticUrl,
        isVisibleInPicker = isVisibleInPicker,
        category = category
    )

fun DatabaseApplication.toExternalModel(): Application =
    Application(
        name = name,
        website = website,
        vapidKey = vapidKey,
        clientId = clientId,
        clientSecret = clientSecret
    )

fun DatabasePoll.toExternalModel(): Poll =
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

fun DatabasePollOption.toExternalModel(): PollOption =
    PollOption(
        title = title,
        votesCount = votesCount
    )

fun DatabaseField.toExternalModel(): Field =
    Field(
        name = name,
        value = value,
        verifiedAt = verifiedAt
    )

fun DatabaseSource.toExternalModel(): Source =
    Source(
        bio = bio,
        fields = fields.map { it.toExternalModel() },
        defaultPrivacy = defaultPrivacy?.toExternalModel(),
        defaultSensitivity = defaultSensitivity,
        defaultLanguage = defaultLanguage,
        followRequestsCount = followRequestsCount
    )

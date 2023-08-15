package org.mozilla.social.core.data.repository.model.status

import org.mozilla.social.core.database.model.DatabaseAccount
import org.mozilla.social.core.database.model.DatabaseApplication
import org.mozilla.social.core.database.model.DatabaseAttachment
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
import org.mozilla.social.core.database.model.wrappers.StatusWrapper
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

fun Status.toDatabaseModel(): DatabaseStatus =
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
        poll = poll?.toDatabaseModel(),
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

//fun Status.toDatabaseBoostedStatus(): DatabaseBoostedStatus = DatabaseBoostedStatus(
//    statusId = statusId,
//    uri = uri,
//    createdAt = createdAt,
//    account = account.toDatabaseModel(),
//    content = content,
//    visibility = visibility.toDatabaseModel(),
//    isSensitive = isSensitive,
//    contentWarningText = contentWarningText,
//    mediaAttachments = mediaAttachments.map { it.toDatabaseModel() },
//    mentions = mentions.map { it.toDatabaseModel() },
//    hashTags = hashTags.map { it.toDatabaseModel() },
//    emojis = emojis.map { it.toDatabaseModel() },
//    boostsCount = boostsCount,
//    favouritesCount = favouritesCount,
//    repliesCount = repliesCount,
//    application = application?.toDatabaseModel(),
//    url = url,
//    inReplyToId = inReplyToId,
//    inReplyToAccountId = inReplyToAccountId,
//    inReplyToAccountName = inReplyToAccountName,
//    poll = poll?.toDatabaseModel(),
//    //TODO map this if we ever need it
//    card = null,
//    language = language,
//    plainText = plainText,
//    isFavourited = isFavourited,
//    isBoosted = isBoosted,
//    isMuted = isMuted,
//    isBookmarked = isBookmarked,
//    isPinned = isPinned,
//)

fun Account.toDatabaseModel(): DatabaseAccount = DatabaseAccount(
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
    emojis = emojis.map { it.toDatabaseModel() },
    createdAt = createdAt,
    lastStatusAt = lastStatusAt,
    statusesCount = statusesCount,
    followersCount = followersCount,
    followingCount = followingCount,
    isDiscoverable = isDiscoverable,
    //TODO fix
//    movedTo = movedTo?.toDatabaseModel(),
    isGroup = isGroup,
    fields = fields?.map { it.toDatabaseModel() },
    isBot = isBot,
    source = source?.toDatabaseModel(),
    isSuspended = isSuspended,
    muteExpiresAt = muteExpiresAt,
)

fun StatusVisibility.toDatabaseModel(): DatabaseStatusVisibility =
    when(this) {
        StatusVisibility.Direct -> DatabaseStatusVisibility.Direct
        StatusVisibility.Private -> DatabaseStatusVisibility.Private
        StatusVisibility.Public -> DatabaseStatusVisibility.Public
        StatusVisibility.Unlisted -> DatabaseStatusVisibility.Unlisted
    }

fun Attachment.toDatabaseModel(): DatabaseAttachment =
    when (this) {
        is Attachment.Image -> DatabaseAttachment.Image(
            attachmentId = attachmentId,
            url = url,
            previewUrl = previewUrl,
            remoteUrl = remoteUrl,
            previewRemoteUrl = previewRemoteUrl,
            textUrl = textUrl,
            description = description,
            blurHash = blurHash
        )
        is Attachment.Gifv -> DatabaseAttachment.Gifv(
            attachmentId = attachmentId,
            url = url,
            previewUrl = previewUrl,
            remoteUrl = remoteUrl,
            previewRemoteUrl = previewRemoteUrl,
            textUrl = textUrl,
            description = description
        )
        is Attachment.Video -> DatabaseAttachment.Video(
            attachmentId = attachmentId,
            url = url,
            previewUrl = previewUrl,
            remoteUrl = remoteUrl,
            previewRemoteUrl = previewRemoteUrl,
            textUrl = textUrl,
            description = description,
            blurHash = blurHash
        )
        is Attachment.Audio -> DatabaseAttachment.Audio(
            attachmentId = attachmentId,
            url = url,
            previewUrl = previewUrl,
            remoteUrl = remoteUrl,
            previewRemoteUrl = previewRemoteUrl,
            textUrl = textUrl,
            description = description,
            blurHash = blurHash
        )
        is Attachment.Unknown -> DatabaseAttachment.Unknown(
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

fun Mention.toDatabaseModel(): DatabaseMention =
    DatabaseMention(
        accountId = accountId,
        username = username,
        acct = acct,
        url = url
    )

fun HashTag.toDatabaseModel(): DatabaseHashTag =
    DatabaseHashTag(
        name = name,
        url = url,
        history = history?.map { it.toDatabaseModel() }
    )

fun History.toDatabaseModel(): DatabaseHistory =
    DatabaseHistory(
        day = day,
        usageCount = usageCount,
        accountCount = accountCount
    )

fun Emoji.toDatabaseModel(): DatabaseEmoji =
    DatabaseEmoji(
        shortCode = shortCode,
        url = url,
        staticUrl = staticUrl,
        isVisibleInPicker = isVisibleInPicker,
        category = category
    )

fun Application.toDatabaseModel(): DatabaseApplication =
    DatabaseApplication(
        name = name,
        website = website,
        vapidKey = vapidKey,
        clientId = clientId,
        clientSecret = clientSecret
    )

fun Poll.toDatabaseModel(): DatabasePoll =
    DatabasePoll(
        pollId = pollId,
        isExpired = isExpired,
        allowsMultipleChoices = allowsMultipleChoices,
        votesCount = votesCount,
        options = options.map { it.toDatabaseModel() },
        emojis = emojis.map { it.toDatabaseModel() },
        expiresAt = expiresAt,
        votersCount = votersCount,
        hasVoted = hasVoted,
        ownVotes = ownVotes
    )

fun PollOption.toDatabaseModel(): DatabasePollOption =
    DatabasePollOption(
        title = title,
        votesCount = votesCount
    )

fun Field.toDatabaseModel(): DatabaseField =
    DatabaseField(
        name = name,
        value = value,
        verifiedAt = verifiedAt
    )

fun Source.toDatabaseModel(): DatabaseSource =
    DatabaseSource(
        bio = bio,
        fields = fields.map { it.toDatabaseModel() },
        defaultPrivacy = defaultPrivacy?.toDatabaseModel(),
        defaultSensitivity = defaultSensitivity,
        defaultLanguage = defaultLanguage,
        followRequestsCount = followRequestsCount
    )

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

fun NetworkStatus.toDatabaseModel(
    isInHomeFeed: Boolean = false,
): DatabaseStatus =
    DatabaseStatus(
        statusId = statusId,
        isInHomeFeed = isInHomeFeed,
        uri = uri,
        createdAt = createdAt,
        account = account.toDatabaseModel(),
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
        //TODO fix
//        boostedStatus = boostedStatus?.toDatabaseModel(),
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

fun NetworkAccount.toDatabaseModel(): DatabaseAccount = DatabaseAccount(
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

fun NetworkStatusVisibility.toDatabaseModel(): DatabaseStatusVisibility =
    when(this) {
        NetworkStatusVisibility.Direct -> DatabaseStatusVisibility.Direct
        NetworkStatusVisibility.Private -> DatabaseStatusVisibility.Private
        NetworkStatusVisibility.Public -> DatabaseStatusVisibility.Public
        NetworkStatusVisibility.Unlisted -> DatabaseStatusVisibility.Unlisted
    }

fun NetworkAttachment.toDatabaseModel(): DatabaseAttachment =
    when (this) {
        is NetworkAttachment.Image -> DatabaseAttachment.Image(
            attachmentId = attachmentId,
            url = url,
            previewUrl = previewUrl,
            remoteUrl = remoteUrl,
            previewRemoteUrl = previewRemoteUrl,
            textUrl = textUrl,
            description = description,
            blurHash = blurHash
        )
        is NetworkAttachment.Gifv -> DatabaseAttachment.Gifv(
            attachmentId = attachmentId,
            url = url,
            previewUrl = previewUrl,
            remoteUrl = remoteUrl,
            previewRemoteUrl = previewRemoteUrl,
            textUrl = textUrl,
            description = description
        )
        is NetworkAttachment.Video -> DatabaseAttachment.Video(
            attachmentId = attachmentId,
            url = url,
            previewUrl = previewUrl,
            remoteUrl = remoteUrl,
            previewRemoteUrl = previewRemoteUrl,
            textUrl = textUrl,
            description = description,
            blurHash = blurHash
        )
        is NetworkAttachment.Audio -> DatabaseAttachment.Audio(
            attachmentId = attachmentId,
            url = url,
            previewUrl = previewUrl,
            remoteUrl = remoteUrl,
            previewRemoteUrl = previewRemoteUrl,
            textUrl = textUrl,
            description = description,
            blurHash = blurHash
        )
        is NetworkAttachment.Unknown -> DatabaseAttachment.Unknown(
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

fun NetworkMention.toDatabaseModel(): DatabaseMention =
    DatabaseMention(
        accountId = accountId,
        username = username,
        acct = acct,
        url = url
    )

fun NetworkHashTag.toDatabaseModel(): DatabaseHashTag =
    DatabaseHashTag(
        name = name,
        url = url,
        history = history?.map { it.toDatabaseModel() }
    )

fun NetworkHistory.toDatabaseModel(): DatabaseHistory =
    DatabaseHistory(
        day = day,
        usageCount = usageCount,
        accountCount = accountCount
    )

fun NetworkEmoji.toDatabaseModel(): DatabaseEmoji =
    DatabaseEmoji(
        shortCode = shortCode,
        url = url,
        staticUrl = staticUrl,
        isVisibleInPicker = isVisibleInPicker,
        category = category
    )

fun NetworkApplication.toDatabaseModel(): DatabaseApplication =
    DatabaseApplication(
        name = name,
        website = website,
        vapidKey = vapidKey,
        clientId = clientId,
        clientSecret = clientSecret
    )

fun NetworkPoll.toDatabaseModel(): DatabasePoll =
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

fun NetworkPollOption.toDatabaseModel(): DatabasePollOption =
    DatabasePollOption(
        title = title,
        votesCount = votesCount
    )

fun NetworkField.toDatabaseModel(): DatabaseField =
    DatabaseField(
        name = name,
        value = value,
        verifiedAt = verifiedAt
    )

fun NetworkSource.toDatabaseModel(): DatabaseSource =
    DatabaseSource(
        bio = bio,
        fields = fields.map { it.toDatabaseModel() },
        defaultPrivacy = defaultPrivacy?.toDatabaseModel(),
        defaultSensitivity = defaultSensitivity,
        defaultLanguage = defaultLanguage,
        followRequestsCount = followRequestsCount
    )

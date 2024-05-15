package social.firefly.core.repository.mastodon.model.status

import social.firefly.core.database.model.DatabaseApplication
import social.firefly.core.database.model.DatabaseAttachment
import social.firefly.core.database.model.DatabaseCard
import social.firefly.core.database.model.DatabaseEmoji
import social.firefly.core.database.model.DatabaseField
import social.firefly.core.database.model.DatabaseFocalPoint
import social.firefly.core.database.model.DatabaseHashTag
import social.firefly.core.database.model.DatabaseHistory
import social.firefly.core.database.model.DatabaseMention
import social.firefly.core.database.model.DatabaseSource
import social.firefly.core.database.model.DatabaseStatusVisibility
import social.firefly.core.database.model.entities.DatabaseAccount
import social.firefly.core.database.model.entities.DatabaseStatus
import social.firefly.core.database.model.entities.accountCollections.DatabaseBlock
import social.firefly.core.database.model.entities.accountCollections.DatabaseMute
import social.firefly.core.model.Account
import social.firefly.core.model.Application
import social.firefly.core.model.Attachment
import social.firefly.core.model.BasicHashTag
import social.firefly.core.model.Card
import social.firefly.core.model.Emoji
import social.firefly.core.model.Field
import social.firefly.core.model.FocalPoint
import social.firefly.core.model.History
import social.firefly.core.model.Mention
import social.firefly.core.model.Source
import social.firefly.core.model.Status
import social.firefly.core.model.StatusVisibility

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
        boostedPollId = boostedStatus?.poll?.pollId,
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

fun Account.toDatabaseModel(): DatabaseAccount =
    DatabaseAccount(
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
        statusesCount = statusesCount,
        followersCount = followersCount,
        followingCount = followingCount,
        isDiscoverable = isDiscoverable,
        // TODO do we need this?  would require some work with the database wrappers
//    movedTo = movedTo?.toDatabaseModel(),
        isGroup = isGroup,
        fields = fields?.map { it.toDatabaseModel() },
        isBot = isBot,
        source = source?.toDatabaseModel(),
        isSuspended = isSuspended,
        muteExpiresAt = muteExpiresAt,
    )

fun Account.toDatabaseBlock(position: Int): DatabaseBlock =
    DatabaseBlock(accountId = accountId, position = position)

fun Account.toDatabaseMute(position: Int): DatabaseMute =
    DatabaseMute(accountId = accountId, position = position)

fun StatusVisibility.toDatabaseModel(): DatabaseStatusVisibility =
    when (this) {
        StatusVisibility.Direct -> DatabaseStatusVisibility.Direct
        StatusVisibility.Private -> DatabaseStatusVisibility.Private
        StatusVisibility.Public -> DatabaseStatusVisibility.Public
        StatusVisibility.Unlisted -> DatabaseStatusVisibility.Unlisted
    }

fun Attachment.toDatabaseModel(): DatabaseAttachment =
    when (this) {
        is Attachment.Image ->
            DatabaseAttachment.Image(
                attachmentId = attachmentId,
                url = url,
                previewUrl = previewUrl,
                remoteUrl = remoteUrl,
                previewRemoteUrl = previewRemoteUrl,
                textUrl = textUrl,
                description = description,
                blurHash = blurHash,
                meta = meta?.toDatabaseModel(),
            )

        is Attachment.Gifv ->
            DatabaseAttachment.Gifv(
                attachmentId = attachmentId,
                url = url,
                previewUrl = previewUrl,
                remoteUrl = remoteUrl,
                previewRemoteUrl = previewRemoteUrl,
                textUrl = textUrl,
                description = description,
                meta = meta?.toDatabaseModel(),
            )

        is Attachment.Video ->
            DatabaseAttachment.Video(
                attachmentId = attachmentId,
                url = url,
                previewUrl = previewUrl,
                remoteUrl = remoteUrl,
                previewRemoteUrl = previewRemoteUrl,
                textUrl = textUrl,
                description = description,
                blurHash = blurHash,
                meta = meta?.toDatabaseModel(),
            )

        is Attachment.Audio ->
            DatabaseAttachment.Audio(
                attachmentId = attachmentId,
                url = url,
                previewUrl = previewUrl,
                remoteUrl = remoteUrl,
                previewRemoteUrl = previewRemoteUrl,
                textUrl = textUrl,
                description = description,
                blurHash = blurHash,
                meta = meta?.toDatabaseModel(),
            )

        is Attachment.Unknown ->
            DatabaseAttachment.Unknown(
                attachmentId = attachmentId,
                url = url,
                previewUrl = previewUrl,
                remoteUrl = remoteUrl,
                previewRemoteUrl = previewRemoteUrl,
                textUrl = textUrl,
                description = description,
                blurHash = blurHash,
            )
    }

fun Attachment.Audio.Meta.toDatabaseModel(): DatabaseAttachment.Audio.Meta =
    DatabaseAttachment.Audio.Meta(
        durationSeconds = durationSeconds,
        audioCodec = audioCodec,
        audioBitrate = audioBitrate,
        audioChannels = audioChannels,
        original = original?.toDatabaseModel(),
    )

fun Attachment.Audio.Meta.AudioInfo.toDatabaseModel(): DatabaseAttachment.Audio.Meta.AudioInfo =
    DatabaseAttachment.Audio.Meta.AudioInfo(
        bitrate = bitrate,
    )

fun Attachment.Video.Meta.toDatabaseModel(): DatabaseAttachment.Video.Meta =
    DatabaseAttachment.Video.Meta(
        aspectRatio = aspectRatio,
        durationSeconds = durationSeconds,
        fps = fps,
        audioCodec = audioCodec,
        audioBitrate = audioBitrate,
        audioChannels = audioChannels,
        original = original?.toDatabaseModel(),
        small = small?.toDatabaseModel(),
    )

fun Attachment.Video.Meta.VideoInfo.toDatabaseModel(): DatabaseAttachment.Video.Meta.VideoInfo =
    DatabaseAttachment.Video.Meta.VideoInfo(
        width = width,
        height = height,
        bitrate = bitrate,
    )

fun Attachment.Image.Meta.toDatabaseModel(): DatabaseAttachment.Image.Meta =
    DatabaseAttachment.Image.Meta(
        focalPoint = focalPoint?.toDatabaseModel(),
        original = original?.toDatabaseModel(),
        small = small?.toDatabaseModel(),
    )

fun Attachment.Image.Meta.ImageInfo.toDatabaseModel(): DatabaseAttachment.Image.Meta.ImageInfo =
    DatabaseAttachment.Image.Meta.ImageInfo(
        width = width,
        height = height,
        size = size,
        aspectRatio = aspectRatio,
    )

fun Attachment.Gifv.Meta.toDatabaseModel(): DatabaseAttachment.Gifv.Meta =
    DatabaseAttachment.Gifv.Meta(
        aspectRatio = aspectRatio,
        durationSeconds = durationSeconds,
        fps = fps,
        bitrate = bitrate,
        original = original?.toDatabaseModel(),
        small = small?.toDatabaseModel(),
    )

fun Attachment.Gifv.Meta.GifvInfo.toDatabaseModel(): DatabaseAttachment.Gifv.Meta.GifvInfo =
    DatabaseAttachment.Gifv.Meta.GifvInfo(
        width = width,
        height = height,
        bitrate = bitrate,
    )

fun FocalPoint.toDatabaseModel(): DatabaseFocalPoint =
    DatabaseFocalPoint(
        x = x,
        y = y,
    )

fun Mention.toDatabaseModel(): DatabaseMention =
    DatabaseMention(
        accountId = accountId,
        username = username,
        acct = acct,
        url = url,
    )

fun BasicHashTag.toDatabaseModel(): DatabaseHashTag =
    DatabaseHashTag(
        name = name,
        url = url,
    )

fun History.toDatabaseModel(): DatabaseHistory =
    DatabaseHistory(
        day = day,
        usageCount = usageCount,
        accountCount = accountCount,
    )

fun Application.toDatabaseModel(): DatabaseApplication =
    DatabaseApplication(
        name = name,
        website = website,
        vapidKey = vapidKey,
        clientId = clientId,
        clientSecret = clientSecret,
    )

fun Emoji.toDatabaseModel(): DatabaseEmoji =
    DatabaseEmoji(
        shortCode = shortCode,
        url = url,
        staticUrl = staticUrl,
        isVisibleInPicker = isVisibleInPicker,
        category = category,
    )

fun Field.toDatabaseModel(): DatabaseField =
    DatabaseField(
        name = name,
        value = value,
        verifiedAt = verifiedAt,
    )

fun Source.toDatabaseModel(): DatabaseSource =
    DatabaseSource(
        bio = bio,
        fields = fields.map { it.toDatabaseModel() },
        defaultPrivacy = defaultPrivacy?.toDatabaseModel(),
        defaultSensitivity = defaultSensitivity,
        defaultLanguage = defaultLanguage,
        followRequestsCount = followRequestsCount,
    )

fun Card.toDatabaseModel(): DatabaseCard =
    when (this) {
        is Card.Video ->
            DatabaseCard.Video(
                url = url,
                title = title,
                description = description,
                authorName = authorName,
                authorUrl = authorUrl,
                providerName = providerName,
                providerUrl = providerUrl,
                html = html,
                width = width,
                height = height,
                image = image,
                embedUrl = embedUrl,
                blurHash = blurHash,
            )

        is Card.Link ->
            DatabaseCard.Link(
                url = url,
                title = title,
                description = description,
                authorName = authorName,
                authorUrl = authorUrl,
                providerName = providerName,
                providerUrl = providerUrl,
                html = html,
                width = width,
                height = height,
                image = image,
                embedUrl = embedUrl,
                blurHash = blurHash,
            )

        is Card.Photo ->
            DatabaseCard.Photo(
                url = url,
                title = title,
                description = description,
                authorName = authorName,
                authorUrl = authorUrl,
                providerName = providerName,
                providerUrl = providerUrl,
                html = html,
                width = width,
                height = height,
                image = image,
                embedUrl = embedUrl,
                blurHash = blurHash,
            )

        is Card.Rich ->
            DatabaseCard.Rich(
                url = url,
                title = title,
                description = description,
                authorName = authorName,
                authorUrl = authorUrl,
                providerName = providerName,
                providerUrl = providerUrl,
                html = html,
                width = width,
                height = height,
                image = image,
                embedUrl = embedUrl,
                blurHash = blurHash,
            )
    }

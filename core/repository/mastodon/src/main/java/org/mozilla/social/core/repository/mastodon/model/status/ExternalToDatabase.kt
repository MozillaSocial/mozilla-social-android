package org.mozilla.social.core.repository.mastodon.model.status

import org.mozilla.social.core.database.model.entities.DatabaseAccount
import org.mozilla.social.core.database.model.DatabaseApplication
import org.mozilla.social.core.database.model.DatabaseAttachment
import org.mozilla.social.core.database.model.DatabaseCard
import org.mozilla.social.core.database.model.DatabaseEmoji
import org.mozilla.social.core.database.model.DatabaseField
import org.mozilla.social.core.database.model.DatabaseFocalPoint
import org.mozilla.social.core.database.model.DatabaseHashTag
import org.mozilla.social.core.database.model.DatabaseHistory
import org.mozilla.social.core.database.model.DatabaseMention
import org.mozilla.social.core.database.model.DatabaseSource
import org.mozilla.social.core.database.model.DatabaseStatusVisibility
import org.mozilla.social.core.database.model.entities.accountCollections.DatabaseBlock
import org.mozilla.social.core.database.model.entities.accountCollections.DatabaseMute
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.model.Application
import org.mozilla.social.core.model.Attachment
import org.mozilla.social.core.model.Card
import org.mozilla.social.core.model.Emoji
import org.mozilla.social.core.model.Field
import org.mozilla.social.core.model.FocalPoint
import org.mozilla.social.core.model.BasicHashTag
import org.mozilla.social.core.model.History
import org.mozilla.social.core.model.Mention
import org.mozilla.social.core.model.Source
import org.mozilla.social.core.model.StatusVisibility

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

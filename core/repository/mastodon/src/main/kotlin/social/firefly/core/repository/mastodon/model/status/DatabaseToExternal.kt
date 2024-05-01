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
import social.firefly.core.database.model.DatabasePollOption
import social.firefly.core.database.model.DatabaseSource
import social.firefly.core.database.model.DatabaseStatusVisibility
import social.firefly.core.database.model.entities.DatabaseAccount
import social.firefly.core.database.model.entities.DatabasePoll
import social.firefly.core.database.model.entities.DatabaseStatus
import social.firefly.core.database.model.wrappers.StatusWrapper
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
import social.firefly.core.model.Poll
import social.firefly.core.model.PollOption
import social.firefly.core.model.Source
import social.firefly.core.model.Status
import social.firefly.core.model.StatusVisibility

fun StatusWrapper.toExternalModel(): Status =
    Status(
        statusId = status.statusId,
        uri = status.uri,
        createdAt = status.createdAt,
        account = account.toExternalModel(),
        content = status.content,
        visibility = status.visibility.toExternalModel(),
        isSensitive = status.isSensitive,
        contentWarningText = status.contentWarningText,
        mediaAttachments = status.mediaAttachments.map { it.toExternalModel() },
        mentions = status.mentions.map { it.toExternalModel() },
        hashTags = status.hashTags.map { it.toExternalModel() },
        emojis = status.emojis.map { it.toExternalModel() },
        boostsCount = status.boostsCount,
        favouritesCount = status.favouritesCount,
        repliesCount = status.repliesCount,
        application = status.application?.toExternalModel(),
        url = status.url,
        inReplyToId = status.inReplyToId,
        inReplyToAccountId = status.inReplyToAccountId,
        inReplyToAccountName = status.inReplyToAccountName,
        boostedStatus = boostedAccount?.let { boostedStatus?.toExternalModel(it, boostedPoll) },
        poll = poll?.toExternalModel(),
        card = status.card?.toExternalModel(),
        language = status.language,
        plainText = status.plainText,
        isFavourited = status.isFavorited,
        isBoosted = status.isBoosted,
        isMuted = status.isMuted,
        isBookmarked = status.isBookmarked,
        isPinned = status.isPinned,
        isBeingDeleted = status.isBeingDeleted,
    )

fun DatabaseStatus.toExternalModel(
    account: DatabaseAccount,
    poll: DatabasePoll?,
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
        poll = poll?.toExternalModel(),
        card = card?.toExternalModel(),
        language = language,
        plainText = plainText,
        isFavourited = isFavorited,
        isBoosted = isBoosted,
        isMuted = isMuted,
        isBookmarked = isBookmarked,
        isPinned = isPinned,
    )

fun DatabaseAccount.toExternalModel(): Account =
    Account(
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
        statusesCount = statusesCount,
        followersCount = followersCount,
        followingCount = followingCount,
        isDiscoverable = isDiscoverable,
        // TODO fix
//    movedTo = movedTo?.toExternalModel(),
        isGroup = isGroup,
        fields = fields?.map { it.toExternalModel() },
        isBot = isBot,
        source = source?.toExternalModel(),
        isSuspended = isSuspended,
        muteExpiresAt = muteExpiresAt,
    )

fun DatabaseStatusVisibility.toExternalModel(): StatusVisibility =
    when (this) {
        DatabaseStatusVisibility.Direct -> StatusVisibility.Direct
        DatabaseStatusVisibility.Private -> StatusVisibility.Private
        DatabaseStatusVisibility.Public -> StatusVisibility.Public
        DatabaseStatusVisibility.Unlisted -> StatusVisibility.Unlisted
    }

fun DatabaseAttachment.toExternalModel(): Attachment =
    when (this) {
        is DatabaseAttachment.Image ->
            Attachment.Image(
                attachmentId = attachmentId,
                url = url,
                previewUrl = previewUrl,
                remoteUrl = remoteUrl,
                previewRemoteUrl = previewRemoteUrl,
                textUrl = textUrl,
                description = description,
                blurHash = blurHash,
                meta = meta?.toExternalModel(),
            )

        is DatabaseAttachment.Gifv ->
            Attachment.Gifv(
                attachmentId = attachmentId,
                url = url,
                previewUrl = previewUrl,
                remoteUrl = remoteUrl,
                previewRemoteUrl = previewRemoteUrl,
                textUrl = textUrl,
                description = description,
                meta = meta?.toExternalModel(),
            )

        is DatabaseAttachment.Video ->
            Attachment.Video(
                attachmentId = attachmentId,
                url = url,
                previewUrl = previewUrl,
                remoteUrl = remoteUrl,
                previewRemoteUrl = previewRemoteUrl,
                textUrl = textUrl,
                description = description,
                blurHash = blurHash,
                meta = meta?.toExternalModel(),
            )

        is DatabaseAttachment.Audio ->
            Attachment.Audio(
                attachmentId = attachmentId,
                url = url,
                previewUrl = previewUrl,
                remoteUrl = remoteUrl,
                previewRemoteUrl = previewRemoteUrl,
                textUrl = textUrl,
                description = description,
                blurHash = blurHash,
                meta = meta?.toExternalModel(),
            )

        is DatabaseAttachment.Unknown ->
            Attachment.Unknown(
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

fun DatabaseAttachment.Audio.Meta.toExternalModel(): Attachment.Audio.Meta =
    Attachment.Audio.Meta(
        durationSeconds = durationSeconds,
        audioCodec = audioCodec,
        audioBitrate = audioBitrate,
        audioChannels = audioChannels,
        original = original?.toExternalModel(),
    )

fun DatabaseAttachment.Audio.Meta.AudioInfo.toExternalModel(): Attachment.Audio.Meta.AudioInfo =
    Attachment.Audio.Meta.AudioInfo(
        bitrate = bitrate,
    )

fun DatabaseAttachment.Video.Meta.toExternalModel(): Attachment.Video.Meta =
    Attachment.Video.Meta(
        aspectRatio = aspectRatio,
        durationSeconds = durationSeconds,
        fps = fps,
        audioCodec = audioCodec,
        audioBitrate = audioBitrate,
        audioChannels = audioChannels,
        original = original?.toExternalModel(),
        small = small?.toExternalModel(),
    )

fun DatabaseAttachment.Video.Meta.VideoInfo.toExternalModel(): Attachment.Video.Meta.VideoInfo =
    Attachment.Video.Meta.VideoInfo(
        width = width,
        height = height,
        bitrate = bitrate,
    )

fun DatabaseAttachment.Image.Meta.toExternalModel(): Attachment.Image.Meta =
    Attachment.Image.Meta(
        focalPoint = focalPoint?.toExternalModel(),
        original = original?.toExternalModel(),
        small = small?.toExternalModel(),
    )

fun DatabaseAttachment.Image.Meta.ImageInfo.toExternalModel(): Attachment.Image.Meta.ImageInfo =
    Attachment.Image.Meta.ImageInfo(
        width = width,
        height = height,
        size = size,
        aspectRatio = aspectRatio,
    )

fun DatabaseAttachment.Gifv.Meta.toExternalModel(): Attachment.Gifv.Meta =
    Attachment.Gifv.Meta(
        aspectRatio = aspectRatio,
        durationSeconds = durationSeconds,
        fps = fps,
        bitrate = bitrate,
        original = original?.toExternalModel(),
        small = small?.toExternalModel(),
    )

fun DatabaseAttachment.Gifv.Meta.GifvInfo.toExternalModel(): Attachment.Gifv.Meta.GifvInfo =
    Attachment.Gifv.Meta.GifvInfo(
        width = width,
        height = height,
        bitrate = bitrate,
    )

fun DatabaseFocalPoint.toExternalModel(): FocalPoint =
    FocalPoint(
        x = x,
        y = y,
    )

fun DatabaseMention.toExternalModel(): Mention =
    Mention(
        accountId = accountId,
        username = username,
        acct = acct,
        url = url,
    )

fun DatabaseHashTag.toExternalModel(): BasicHashTag =
    BasicHashTag(
        name = name,
        url = url,
    )

fun DatabaseHistory.toExternalModel(): History =
    History(
        day = day,
        usageCount = usageCount,
        accountCount = accountCount,
    )

fun DatabaseEmoji.toExternalModel(): Emoji =
    Emoji(
        shortCode = shortCode,
        url = url,
        staticUrl = staticUrl,
        isVisibleInPicker = isVisibleInPicker,
        category = category,
    )

fun DatabaseApplication.toExternalModel(): Application =
    Application(
        name = name,
        website = website,
        vapidKey = vapidKey,
        clientId = clientId,
        clientSecret = clientSecret,
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
        ownVotes = ownVotes,
    )

fun DatabasePollOption.toExternalModel(): PollOption =
    PollOption(
        title = title,
        votesCount = votesCount,
    )

fun DatabaseField.toExternalModel(): Field =
    Field(
        name = name,
        value = value,
        verifiedAt = verifiedAt,
    )

fun DatabaseSource.toExternalModel(): Source =
    Source(
        bio = bio,
        fields = fields.map { it.toExternalModel() },
        defaultPrivacy = defaultPrivacy?.toExternalModel(),
        defaultSensitivity = defaultSensitivity,
        defaultLanguage = defaultLanguage,
        followRequestsCount = followRequestsCount,
    )

fun DatabaseCard.toExternalModel(): Card =
    when (this) {
        is DatabaseCard.Video ->
            Card.Video(
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

        is DatabaseCard.Link ->
            Card.Link(
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

        is DatabaseCard.Photo ->
            Card.Photo(
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

        is DatabaseCard.Rich ->
            Card.Rich(
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

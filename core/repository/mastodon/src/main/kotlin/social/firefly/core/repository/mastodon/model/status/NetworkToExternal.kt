package social.firefly.core.repository.mastodon.model.status

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
import social.firefly.core.network.mastodon.model.responseBody.NetworkAccount
import social.firefly.core.network.mastodon.model.responseBody.NetworkApplication
import social.firefly.core.network.mastodon.model.responseBody.NetworkAttachment
import social.firefly.core.network.mastodon.model.responseBody.NetworkBasicHashTag
import social.firefly.core.network.mastodon.model.responseBody.NetworkCard
import social.firefly.core.network.mastodon.model.responseBody.NetworkEmoji
import social.firefly.core.network.mastodon.model.responseBody.NetworkField
import social.firefly.core.network.mastodon.model.responseBody.NetworkFocalPoint
import social.firefly.core.network.mastodon.model.responseBody.NetworkHistory
import social.firefly.core.network.mastodon.model.responseBody.NetworkMention
import social.firefly.core.network.mastodon.model.responseBody.NetworkPoll
import social.firefly.core.network.mastodon.model.responseBody.NetworkPollOption
import social.firefly.core.network.mastodon.model.responseBody.NetworkSource
import social.firefly.core.network.mastodon.model.responseBody.NetworkStatus
import social.firefly.core.network.mastodon.model.responseBody.NetworkStatusVisibility

fun social.firefly.core.network.mastodon.model.responseBody.NetworkStatus.toExternalModel(): Status =
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
        card = card?.toExternalModel(),
        language = language,
        plainText = plainText,
        isFavourited = isFavourited,
        isBoosted = isBoosted,
        isMuted = isMuted,
        isBookmarked = isBookmarked,
        isPinned = isPinned,
    )

fun social.firefly.core.network.mastodon.model.responseBody.NetworkAccount.toExternalModel(): Account =
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
        movedTo = movedTo?.toExternalModel(),
        isGroup = isGroup,
        fields = fields?.map { it.toExternalModel() },
        isBot = isBot,
        source = source?.toExternalModel(),
        isSuspended = isSuspended,
        muteExpiresAt = muteExpiresAt,
    )

fun social.firefly.core.network.mastodon.model.responseBody.NetworkStatusVisibility.toExternalModel(): StatusVisibility =
    when (this) {
        social.firefly.core.network.mastodon.model.responseBody.NetworkStatusVisibility.Direct -> StatusVisibility.Direct
        social.firefly.core.network.mastodon.model.responseBody.NetworkStatusVisibility.Private -> StatusVisibility.Private
        social.firefly.core.network.mastodon.model.responseBody.NetworkStatusVisibility.Public -> StatusVisibility.Public
        social.firefly.core.network.mastodon.model.responseBody.NetworkStatusVisibility.Unlisted -> StatusVisibility.Unlisted
    }

fun social.firefly.core.network.mastodon.model.responseBody.NetworkAttachment.toExternalModel(): Attachment =
    when (this) {
        is social.firefly.core.network.mastodon.model.responseBody.NetworkAttachment.Image ->
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

        is social.firefly.core.network.mastodon.model.responseBody.NetworkAttachment.Gifv ->
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

        is social.firefly.core.network.mastodon.model.responseBody.NetworkAttachment.Video ->
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

        is social.firefly.core.network.mastodon.model.responseBody.NetworkAttachment.Audio ->
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

        is social.firefly.core.network.mastodon.model.responseBody.NetworkAttachment.Unknown ->
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

fun social.firefly.core.network.mastodon.model.responseBody.NetworkAttachment.Audio.Meta.toExternalModel(): Attachment.Audio.Meta =
    Attachment.Audio.Meta(
        durationSeconds = durationSeconds,
        audioCodec = audioCodec,
        audioBitrate = audioBitrate,
        audioChannels = audioChannels,
        original = original?.toExternalModel(),
    )

fun social.firefly.core.network.mastodon.model.responseBody.NetworkAttachment.Audio.Meta.AudioInfo.toExternalModel(): Attachment.Audio.Meta.AudioInfo =
    Attachment.Audio.Meta.AudioInfo(
        bitrate = bitrate,
    )

fun social.firefly.core.network.mastodon.model.responseBody.NetworkAttachment.Video.Meta.toExternalModel(): Attachment.Video.Meta =
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

fun social.firefly.core.network.mastodon.model.responseBody.NetworkAttachment.Video.Meta.VideoInfo.toExternalModel(): Attachment.Video.Meta.VideoInfo =
    Attachment.Video.Meta.VideoInfo(
        width = width,
        height = height,
        bitrate = bitrate,
    )

fun social.firefly.core.network.mastodon.model.responseBody.NetworkAttachment.Image.Meta.toExternalModel(): Attachment.Image.Meta =
    Attachment.Image.Meta(
        focalPoint = focalPoint?.toExternalModel(),
        original = original?.toExternalModel(),
        small = small?.toExternalModel(),
    )

fun social.firefly.core.network.mastodon.model.responseBody.NetworkAttachment.Image.Meta.ImageInfo.toExternalModel(): Attachment.Image.Meta.ImageInfo =
    Attachment.Image.Meta.ImageInfo(
        width = width,
        height = height,
        size = size,
        aspectRatio = aspectRatio,
    )

fun social.firefly.core.network.mastodon.model.responseBody.NetworkAttachment.Gifv.Meta.toExternalModel(): Attachment.Gifv.Meta =
    Attachment.Gifv.Meta(
        aspectRatio = aspectRatio,
        durationSeconds = durationSeconds,
        fps = fps,
        bitrate = bitrate,
        original = original?.toExternalModel(),
        small = small?.toExternalModel(),
    )

fun social.firefly.core.network.mastodon.model.responseBody.NetworkAttachment.Gifv.Meta.GifvInfo.toExternalModel(): Attachment.Gifv.Meta.GifvInfo =
    Attachment.Gifv.Meta.GifvInfo(
        width = width,
        height = height,
        bitrate = bitrate,
    )

fun social.firefly.core.network.mastodon.model.responseBody.NetworkFocalPoint.toExternalModel(): FocalPoint =
    FocalPoint(
        x = x,
        y = y,
    )

fun social.firefly.core.network.mastodon.model.responseBody.NetworkMention.toExternalModel(): Mention =
    Mention(
        accountId = accountId,
        username = username,
        acct = acct,
        url = url,
    )

fun social.firefly.core.network.mastodon.model.responseBody.NetworkBasicHashTag.toExternalModel(): BasicHashTag =
    BasicHashTag(
        name = name,
        url = url,
    )

fun social.firefly.core.network.mastodon.model.responseBody.NetworkHistory.toExternalModel(): History =
    History(
        day = day,
        usageCount = usageCount,
        accountCount = accountCount,
    )

fun social.firefly.core.network.mastodon.model.responseBody.NetworkEmoji.toExternalModel(): Emoji =
    Emoji(
        shortCode = shortCode,
        url = url,
        staticUrl = staticUrl,
        isVisibleInPicker = isVisibleInPicker,
        category = category,
    )

fun social.firefly.core.network.mastodon.model.responseBody.NetworkApplication.toExternalModel(): Application =
    Application(
        name = name,
        website = website,
        vapidKey = vapidKey,
        clientId = clientId,
        clientSecret = clientSecret,
    )

fun social.firefly.core.network.mastodon.model.responseBody.NetworkPoll.toExternalModel(): Poll =
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

fun social.firefly.core.network.mastodon.model.responseBody.NetworkPollOption.toExternalModel(): PollOption =
    PollOption(
        title = title,
        votesCount = votesCount,
    )

fun social.firefly.core.network.mastodon.model.responseBody.NetworkField.toExternalModel(): Field =
    Field(
        name = name,
        value = value,
        verifiedAt = verifiedAt,
    )

fun social.firefly.core.network.mastodon.model.responseBody.NetworkSource.toExternalModel(): Source =
    Source(
        bio = bio,
        fields = fields.map { it.toExternalModel() },
        defaultPrivacy = defaultPrivacy?.toExternalModel(),
        defaultSensitivity = defaultSensitivity,
        defaultLanguage = defaultLanguage,
        followRequestsCount = followRequestsCount,
    )

fun social.firefly.core.network.mastodon.model.responseBody.NetworkCard.toExternalModel(): Card =
    when (type) {
        "video" ->
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

        "link" ->
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

        "photo" ->
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

        "rich" ->
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

        else -> error("type value is incorrect")
    }

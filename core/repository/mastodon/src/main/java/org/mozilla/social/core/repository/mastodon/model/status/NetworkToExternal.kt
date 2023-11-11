package org.mozilla.social.core.repository.mastodon.model.status

import org.mozilla.social.core.network.mastodon.model.NetworkAccount
import org.mozilla.social.core.network.mastodon.model.NetworkApplication
import org.mozilla.social.core.network.mastodon.model.NetworkAttachment
import org.mozilla.social.core.network.mastodon.model.NetworkCard
import org.mozilla.social.core.network.mastodon.model.NetworkEmoji
import org.mozilla.social.core.network.mastodon.model.NetworkField
import org.mozilla.social.core.network.mastodon.model.NetworkFocalPoint
import org.mozilla.social.core.network.mastodon.model.NetworkHashTag
import org.mozilla.social.core.network.mastodon.model.NetworkHistory
import org.mozilla.social.core.network.mastodon.model.NetworkMention
import org.mozilla.social.core.network.mastodon.model.NetworkPoll
import org.mozilla.social.core.network.mastodon.model.NetworkPollOption
import org.mozilla.social.core.network.mastodon.model.NetworkSource
import org.mozilla.social.core.network.mastodon.model.NetworkStatus
import org.mozilla.social.core.network.mastodon.model.NetworkStatusVisibility
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.model.Application
import org.mozilla.social.core.model.Attachment
import org.mozilla.social.core.model.Card
import org.mozilla.social.core.model.Emoji
import org.mozilla.social.core.model.Field
import org.mozilla.social.core.model.FocalPoint
import org.mozilla.social.core.model.HashTag
import org.mozilla.social.core.model.History
import org.mozilla.social.core.model.Mention
import org.mozilla.social.core.model.Poll
import org.mozilla.social.core.model.PollOption
import org.mozilla.social.core.model.Source
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.model.StatusVisibility

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
        card = card?.toExternalModel(),
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
            blurHash = blurHash,
            meta = meta.toExternalModel(),
        )
        is NetworkAttachment.Gifv -> Attachment.Gifv(
            attachmentId = attachmentId,
            url = url,
            previewUrl = previewUrl,
            remoteUrl = remoteUrl,
            previewRemoteUrl = previewRemoteUrl,
            textUrl = textUrl,
            description = description,
            meta = meta.toExternalModel(),
        )
        is NetworkAttachment.Video -> Attachment.Video(
            attachmentId = attachmentId,
            url = url,
            previewUrl = previewUrl,
            remoteUrl = remoteUrl,
            previewRemoteUrl = previewRemoteUrl,
            textUrl = textUrl,
            description = description,
            blurHash = blurHash,
            meta = meta.toExternalModel(),
        )
        is NetworkAttachment.Audio -> Attachment.Audio(
            attachmentId = attachmentId,
            url = url,
            previewUrl = previewUrl,
            remoteUrl = remoteUrl,
            previewRemoteUrl = previewRemoteUrl,
            textUrl = textUrl,
            description = description,
            blurHash = blurHash,
            meta = meta.toExternalModel(),
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

fun NetworkAttachment.Audio.Meta.toExternalModel(): Attachment.Audio.Meta =
    Attachment.Audio.Meta(
        durationSeconds = durationSeconds,
        audioCodec = audioCodec,
        audioBitrate = audioBitrate,
        audioChannels = audioChannels,
        original = original?.toExternalModel()
    )

fun NetworkAttachment.Audio.Meta.AudioInfo.toExternalModel(): Attachment.Audio.Meta.AudioInfo =
    Attachment.Audio.Meta.AudioInfo(
        bitrate = bitrate,
    )

fun NetworkAttachment.Video.Meta.toExternalModel(): Attachment.Video.Meta =
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

fun NetworkAttachment.Video.Meta.VideoInfo.toExternalModel(): Attachment.Video.Meta.VideoInfo =
    Attachment.Video.Meta.VideoInfo(
        width = width,
        height = height,
        bitrate = bitrate,
    )

fun NetworkAttachment.Image.Meta.toExternalModel(): Attachment.Image.Meta =
    Attachment.Image.Meta(
        focalPoint = focalPoint?.toExternalModel(),
        original = original?.toExternalModel(),
        small = small?.toExternalModel(),
    )

fun NetworkAttachment.Image.Meta.ImageInfo.toExternalModel(): Attachment.Image.Meta.ImageInfo =
    Attachment.Image.Meta.ImageInfo(
        width = width,
        height = height,
        size = size,
        aspectRatio = aspectRatio,
    )

fun NetworkAttachment.Gifv.Meta.toExternalModel(): Attachment.Gifv.Meta =
    Attachment.Gifv.Meta(
        aspectRatio = aspectRatio,
        durationSeconds = durationSeconds,
        fps = fps,
        bitrate = bitrate,
        original = original?.toExternalModel(),
        small = small?.toExternalModel(),
    )

fun NetworkAttachment.Gifv.Meta.GifvInfo.toExternalModel(): Attachment.Gifv.Meta.GifvInfo =
    Attachment.Gifv.Meta.GifvInfo(
        width = width,
        height = height,
        bitrate = bitrate,
    )

fun NetworkFocalPoint.toExternalModel(): FocalPoint =
    FocalPoint(
        x = x,
        y = y,
    )

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

fun NetworkCard.toExternalModel(): Card =
    when (type) {
        "video" -> Card.Video(
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
            blurHash = blurHash
        )
        "link" -> Card.Link(
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
            blurHash = blurHash
        )
        "photo" -> Card.Photo(
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
            blurHash = blurHash
        )
        "rich" -> Card.Rich(
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
            blurHash = blurHash
        )
        else -> error("type value is incorrect")
    }
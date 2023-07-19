package org.mozilla.social.core.network.media

import io.ktor.utils.io.ByteReadChannel
import org.mozilla.social.model.entity.Attachment
import org.mozilla.social.model.entity.FocalPoint
import org.mozilla.social.model.entity.request.File

fun File.toMastodonk(): fr.outadoc.mastodonk.api.entity.request.File =
    fr.outadoc.mastodonk.api.entity.request.File(
        ByteReadChannel(contents), filename, contentType
    )

fun fr.outadoc.mastodonk.api.entity.Attachment.toDomain(): Attachment =
    when (this) {
        is fr.outadoc.mastodonk.api.entity.Attachment.Image -> Attachment.Image(
            attachmentId, url, previewUrl, remoteUrl, previewRemoteUrl, textUrl, description, blurHash, meta.toDomain()
        )
        is fr.outadoc.mastodonk.api.entity.Attachment.Audio -> Attachment.Audio(
            attachmentId, url, previewUrl, remoteUrl, previewRemoteUrl, textUrl, description, blurHash, meta.toDomain()
        )
        is fr.outadoc.mastodonk.api.entity.Attachment.Gifv -> Attachment.Gifv(
            attachmentId, url, previewUrl, remoteUrl, previewRemoteUrl, textUrl, description, blurHash, meta.toDomain()
        )
        is fr.outadoc.mastodonk.api.entity.Attachment.Unknown -> Attachment.Unknown(
            attachmentId, url, previewUrl, remoteUrl, previewRemoteUrl, textUrl, description, blurHash
        )
        is fr.outadoc.mastodonk.api.entity.Attachment.Video -> Attachment.Video(
            attachmentId, url, previewUrl, remoteUrl, previewRemoteUrl, textUrl, description, blurHash, meta.toDomain()
        )
    }

fun fr.outadoc.mastodonk.api.entity.Attachment.Image.Meta.toDomain(): Attachment.Image.Meta =
    Attachment.Image.Meta(
        width, height, aspect, focalPoint?.toDomain(), original?.toDomain(), small?.toDomain()
    )

fun fr.outadoc.mastodonk.api.entity.Attachment.Audio.Meta.toDomain(): Attachment.Audio.Meta =
    Attachment.Audio.Meta(
        durationSeconds, audioCodec, audioBitrate, audioChannels, bitrate, original?.toDomain()
    )

fun fr.outadoc.mastodonk.api.entity.Attachment.Gifv.Meta.toDomain(): Attachment.Gifv.Meta =
    Attachment.Gifv.Meta(
        width, height, aspect, durationSeconds, fps, bitrate, original?.toDomain(), small?.toDomain()
    )

fun fr.outadoc.mastodonk.api.entity.Attachment.Video.Meta.toDomain(): Attachment.Video.Meta =
    Attachment.Video.Meta(
        width, height, aspect, durationSeconds, fps, audioCodec, audioBitrate, audioChannels, bitrate, original?.toDomain(), small?.toDomain()
    )

fun fr.outadoc.mastodonk.api.entity.FocalPoint.toDomain(): FocalPoint =
    FocalPoint(x, y)
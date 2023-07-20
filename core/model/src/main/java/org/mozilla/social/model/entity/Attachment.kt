package org.mozilla.social.model.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a file or media attachment that can be added to a status.
 */
@Serializable
sealed class Attachment {

    abstract val attachmentId: String

    /**
     * URL of the original full-size attachment.
     */
    abstract val url: String

    /**
     * URL of a scaled-down preview of the attachment.
     */
    abstract val previewUrl: String?

    /**
     * URL of the full-size original attachment on the remote website.
     */
    abstract val remoteUrl: String?

    /**
     * URL of a scaled-down preview of the attachment on the remote website.
     */
    abstract val previewRemoteUrl: String?

    /**
     * A shorter URL for the attachment.
     */
    abstract val textUrl: String?

    /**
     * Alternate text that describes what is in the media attachment.
     *
     * To be used for the visually impaired or when media attachments do not load.
     */
    abstract val description: String?

    /**
     * A hash computed by the BlurHash algorithm, for generating
     * colorful preview thumbnails when media has not been downloaded yet.
     */
    abstract val blurHash: String?

    @Serializable
    @SerialName("image")
    data class Image(
        @SerialName("id")
        override val attachmentId: String,
        @SerialName("url")
        override val url: String,
        @SerialName("preview_url")
        override val previewUrl: String? = null,
        @SerialName("remote_url")
        override val remoteUrl: String? = null,
        @SerialName("preview_remote_url")
        override val previewRemoteUrl: String? = null,
        @SerialName("text_url")
        override val textUrl: String? = null,
        @SerialName("description")
        override val description: String? = null,
        @SerialName("blurhash")
        override val blurHash: String? = null,
        @SerialName("meta")
        val meta: Meta = Meta(),
    ) : Attachment() {
        @Serializable
        data class Meta(
            @SerialName("width")
            val width: Long? = null,
            @SerialName("height")
            val height: Long? = null,
            @SerialName("aspect")
            val aspect: Double? = null,
            @SerialName("focus")
            val focalPoint: FocalPoint? = null,
            @SerialName("original")
            val original: Meta? = null,
            @SerialName("small")
            val small: Meta? = null,
        )
    }

    @Serializable
    @SerialName("video")
    data class Video(
        @SerialName("id")
        override val attachmentId: String,
        @SerialName("url")
        override val url: String,
        @SerialName("preview_url")
        override val previewUrl: String? = null,
        @SerialName("remote_url")
        override val remoteUrl: String? = null,
        @SerialName("preview_remote_url")
        override val previewRemoteUrl: String? = null,
        @SerialName("text_url")
        override val textUrl: String? = null,
        @SerialName("description")
        override val description: String? = null,
        @SerialName("blurhash")
        override val blurHash: String? = null,
        @SerialName("meta")
        val meta: Meta = Meta(),
    ) : Attachment() {
        @Serializable
        data class Meta(
            @SerialName("width")
            val width: Long? = null,
            @SerialName("height")
            val height: Long? = null,
            @SerialName("aspect")
            val aspect: Double? = null,
            @SerialName("duration")
            val durationSeconds: Double? = null,
            @SerialName("fps")
            val fps: Long? = null,
            @SerialName("audio_encode")
            val audioCodec: String? = null,
            @SerialName("audio_bitrate")
            val audioBitrate: String? = null,
            @SerialName("audio_channels")
            val audioChannels: String? = null,
            @SerialName("bitrate")
            val bitrate: Long? = null,
            @SerialName("original")
            val original: Meta? = null,
            @SerialName("small")
            val small: Meta? = null,
        )
    }

    @Serializable
    @SerialName("gifv")
    data class Gifv(
        @SerialName("id")
        override val attachmentId: String,
        @SerialName("url")
        override val url: String,
        @SerialName("preview_url")
        override val previewUrl: String? = null,
        @SerialName("remote_url")
        override val remoteUrl: String? = null,
        @SerialName("preview_remote_url")
        override val previewRemoteUrl: String? = null,
        @SerialName("text_url")
        override val textUrl: String? = null,
        @SerialName("description")
        override val description: String? = null,
        @SerialName("blurhash")
        override val blurHash: String? = null,
        @SerialName("meta")
        val meta: Meta = Meta(),
    ) : Attachment() {
        @Serializable
        data class Meta(
            @SerialName("width")
            val width: Long? = null,
            @SerialName("height")
            val height: Long? = null,
            @SerialName("aspect")
            val aspect: Double? = null,
            @SerialName("duration")
            val durationSeconds: Double? = null,
            @SerialName("fps")
            val fps: Long? = null,
            @SerialName("bitrate")
            val bitrate: Long? = null,
            @SerialName("original")
            val original: Meta? = null,
            @SerialName("small")
            val small: Meta? = null,
        )
    }

    @Serializable
    @SerialName("audio")
    data class Audio(
        @SerialName("id")
        override val attachmentId: String,
        @SerialName("url")
        override val url: String,
        @SerialName("preview_url")
        override val previewUrl: String? = null,
        @SerialName("remote_url")
        override val remoteUrl: String? = null,
        @SerialName("preview_remote_url")
        override val previewRemoteUrl: String? = null,
        @SerialName("text_url")
        override val textUrl: String? = null,
        @SerialName("description")
        override val description: String? = null,
        @SerialName("blurhash")
        override val blurHash: String? = null,
        @SerialName("meta")
        val meta: Meta = Meta(),
    ) : Attachment() {
        @Serializable
        data class Meta(
            @SerialName("duration")
            val durationSeconds: Double? = null,
            @SerialName("audio_encode")
            val audioCodec: String? = null,
            @SerialName("audio_bitrate")
            val audioBitrate: String? = null,
            @SerialName("audio_channels")
            val audioChannels: String? = null,
            @SerialName("bitrate")
            val bitrate: Long? = null,
            @SerialName("original")
            val original: Meta? = null,
        )
    }

    @Serializable
    @SerialName("unknown")
    data class Unknown(
        @SerialName("id")
        override val attachmentId: String,
        @SerialName("url")
        override val url: String,
        @SerialName("preview_url")
        override val previewUrl: String? = null,
        @SerialName("remote_url")
        override val remoteUrl: String? = null,
        @SerialName("preview_remote_url")
        override val previewRemoteUrl: String? = null,
        @SerialName("text_url")
        override val textUrl: String? = null,
        @SerialName("description")
        override val description: String? = null,
        @SerialName("blurhash")
        override val blurHash: String? = null,
    ) : Attachment()
}

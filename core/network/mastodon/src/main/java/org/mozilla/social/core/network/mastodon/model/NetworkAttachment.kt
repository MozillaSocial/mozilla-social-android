package org.mozilla.social.core.network.mastodon.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a file or media attachment that can be added to a status.
 */
@Serializable
sealed class NetworkAttachment {

    abstract val attachmentId: String

    /**
     * URL of the original full-size attachment.
     */
    abstract val url: String?

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
        override val url: String? = null,
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
    ) : NetworkAttachment() {
        @Serializable
        data class Meta(
            @SerialName("focus")
            val focalPoint: NetworkFocalPoint? = null,
            @SerialName("original")
            val original: ImageInfo? = null,
            @SerialName("small")
            val small: ImageInfo? = null,
        ) {
            @Serializable
            data class ImageInfo(
                @SerialName("width")
                val width: Int? = null,
                @SerialName("height")
                val height: Int? = null,
                @SerialName("size")
                val size: String? = null,
                @SerialName("aspect")
                val aspectRatio: Float? = null,
            )
        }
    }

    @Serializable
    @SerialName("video")
    data class Video(
        @SerialName("id")
        override val attachmentId: String,
        @SerialName("url")
        override val url: String? = null,
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
        val meta: NetworkAttachment.Video.Meta = NetworkAttachment.Video.Meta(),
    ) : NetworkAttachment() {
        @Serializable
        data class Meta(
            @SerialName("aspect")
            val aspectRatio: Float? = null,
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
            @SerialName("original")
            val original: NetworkAttachment.Video.Meta.VideoInfo? = null,
            @SerialName("small")
            val small: NetworkAttachment.Video.Meta.VideoInfo? = null,
        ) {
            @Serializable
            data class VideoInfo(
                @SerialName("width")
                val width: Long? = null,
                @SerialName("height")
                val height: Long? = null,
                @SerialName("bitrate")
                val bitrate: Long? = null,
            )
        }
    }

    @Serializable
    @SerialName("gifv")
    data class Gifv(
        @SerialName("id")
        override val attachmentId: String,
        @SerialName("url")
        override val url: String? = null,
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
        val meta: NetworkAttachment.Gifv.Meta = NetworkAttachment.Gifv.Meta(),
    ) : NetworkAttachment() {
        @Serializable
        data class Meta(
            @SerialName("aspect")
            val aspectRatio: Float? = null,
            @SerialName("duration")
            val durationSeconds: Double? = null,
            @SerialName("fps")
            val fps: Long? = null,
            @SerialName("bitrate")
            val bitrate: Long? = null,
            @SerialName("original")
            val original: NetworkAttachment.Gifv.Meta.GifvInfo? = null,
            @SerialName("small")
            val small: NetworkAttachment.Gifv.Meta.GifvInfo? = null,
        ) {
            @Serializable
            data class GifvInfo(
                @SerialName("width")
                val width: Long? = null,
                @SerialName("height")
                val height: Long? = null,
                @SerialName("bitrate")
                val bitrate: Long? = null,
            )
        }
    }

    @Serializable
    @SerialName("audio")
    data class Audio(
        @SerialName("id")
        override val attachmentId: String,
        @SerialName("url")
        override val url: String? = null,
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
        val meta: NetworkAttachment.Audio.Meta = NetworkAttachment.Audio.Meta(),
    ) : NetworkAttachment() {
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
            @SerialName("original")
            val original: NetworkAttachment.Audio.Meta.AudioInfo? = null,
        ) {
            @Serializable
            data class AudioInfo(
                @SerialName("bitrate")
                val bitrate: Long? = null,
            )
        }
    }

    @Serializable
    @SerialName("unknown")
    data class Unknown(
        @SerialName("id")
        override val attachmentId: String,
        @SerialName("url")
        override val url: String? = null,
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
    ) : NetworkAttachment()
}

package org.mozilla.social.core.database.model

import kotlinx.serialization.Serializable

/**
 * Represents a file or media attachment that can be added to a status.
 */
@Serializable
sealed class DatabaseAttachment {
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
    data class Image(
        override val attachmentId: String,
        override val url: String? = null,
        override val previewUrl: String? = null,
        override val remoteUrl: String? = null,
        override val previewRemoteUrl: String? = null,
        override val textUrl: String? = null,
        override val description: String? = null,
        override val blurHash: String? = null,
        val meta: Meta = Meta(),
    ) : DatabaseAttachment() {
        @Serializable
        data class Meta(
            val focalPoint: DatabaseFocalPoint? = null,
            val original: ImageInfo? = null,
            val small: ImageInfo? = null,
        ) {
            @Serializable
            data class ImageInfo(
                val width: Int? = null,
                val height: Int? = null,
                val size: String? = null,
                val aspectRatio: Float? = null,
            )
        }
    }

    @Serializable
    data class Video(
        override val attachmentId: String,
        override val url: String? = null,
        override val previewUrl: String? = null,
        override val remoteUrl: String? = null,
        override val previewRemoteUrl: String? = null,
        override val textUrl: String? = null,
        override val description: String? = null,
        override val blurHash: String? = null,
        val meta: Meta = Meta(),
    ) : DatabaseAttachment() {
        @Serializable
        data class Meta(
            val aspectRatio: Float? = null,
            val durationSeconds: Double? = null,
            val fps: Long? = null,
            val audioCodec: String? = null,
            val audioBitrate: String? = null,
            val audioChannels: String? = null,
            val original: VideoInfo? = null,
            val small: VideoInfo? = null,
        ) {
            @Serializable
            data class VideoInfo(
                val width: Long? = null,
                val height: Long? = null,
                val bitrate: Long? = null,
            )
        }
    }

    @Serializable
    data class Gifv(
        override val attachmentId: String,
        override val url: String? = null,
        override val previewUrl: String? = null,
        override val remoteUrl: String? = null,
        override val previewRemoteUrl: String? = null,
        override val textUrl: String? = null,
        override val description: String? = null,
        override val blurHash: String? = null,
        val meta: Meta = Meta(),
    ) : DatabaseAttachment() {
        @Serializable
        data class Meta(
            val aspectRatio: Float? = null,
            val durationSeconds: Double? = null,
            val fps: Long? = null,
            val bitrate: Long? = null,
            val original: GifvInfo? = null,
            val small: GifvInfo? = null,
        ) {
            @Serializable
            data class GifvInfo(
                val width: Long? = null,
                val height: Long? = null,
                val bitrate: Long? = null,
            )
        }
    }

    @Serializable
    data class Audio(
        override val attachmentId: String,
        override val url: String? = null,
        override val previewUrl: String? = null,
        override val remoteUrl: String? = null,
        override val previewRemoteUrl: String? = null,
        override val textUrl: String? = null,
        override val description: String? = null,
        override val blurHash: String? = null,
        val meta: Meta = Meta(),
    ) : DatabaseAttachment() {
        @Serializable
        data class Meta(
            val durationSeconds: Double? = null,
            val audioCodec: String? = null,
            val audioBitrate: String? = null,
            val audioChannels: String? = null,
            val original: AudioInfo? = null,
        ) {
            @Serializable
            data class AudioInfo(
                val bitrate: Long? = null,
            )
        }
    }

    @Serializable
    data class Unknown(
        override val attachmentId: String,
        override val url: String? = null,
        override val previewUrl: String? = null,
        override val remoteUrl: String? = null,
        override val previewRemoteUrl: String? = null,
        override val textUrl: String? = null,
        override val description: String? = null,
        override val blurHash: String? = null,
    ) : DatabaseAttachment()
}

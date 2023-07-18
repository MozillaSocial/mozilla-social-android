package org.mozilla.social.model.entity

/**
 * Represents a file or media attachment that can be added to a status.
 */
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

    data class Image(
        override val attachmentId: String,
        override val url: String,
        override val previewUrl: String? = null,
        override val remoteUrl: String? = null,
        override val previewRemoteUrl: String? = null,
        override val textUrl: String? = null,
        override val description: String? = null,
        override val blurHash: String? = null,
        val meta: Meta = Meta()
    ) : Attachment() {
        data class Meta(
            val width: Long? = null,
            val height: Long? = null,
            val aspect: Double? = null,
            val focalPoint: FocalPoint? = null,
            val original: Meta? = null,
            val small: Meta? = null
        )
    }

    data class Video(
        override val attachmentId: String,
        override val url: String,
        override val previewUrl: String? = null,
        override val remoteUrl: String? = null,
        override val previewRemoteUrl: String? = null,
        override val textUrl: String? = null,
        override val description: String? = null,
        override val blurHash: String? = null,
        val meta: Meta = Meta()
    ) : Attachment() {
        data class Meta(
            val width: Long? = null,
            val height: Long? = null,
            val aspect: Double? = null,
            val durationSeconds: Double? = null,
            val fps: Long? = null,
            val audioCodec: String? = null,
            val audioBitrate: String? = null,
            val audioChannels: String? = null,
            val bitrate: Long? = null,
            val original: Meta? = null,
            val small: Meta? = null
        )
    }

    data class Gifv(
        override val attachmentId: String,
        override val url: String,
        override val previewUrl: String? = null,
        override val remoteUrl: String? = null,
        override val previewRemoteUrl: String? = null,
        override val textUrl: String? = null,
        override val description: String? = null,
        override val blurHash: String? = null,
        val meta: Meta = Meta()
    ) : Attachment() {

        data class Meta(
            val width: Long? = null,
            val height: Long? = null,
            val aspect: Double? = null,
            val durationSeconds: Double? = null,
            val fps: Long? = null,
            val bitrate: Long? = null,
            val original: Meta? = null,
            val small: Meta? = null
        )
    }

    data class Audio(
        override val attachmentId: String,
        override val url: String,
        override val previewUrl: String? = null,
        override val remoteUrl: String? = null,
        override val previewRemoteUrl: String? = null,
        override val textUrl: String? = null,
        override val description: String? = null,
        override val blurHash: String? = null,
        val meta: Meta = Meta()
    ) : Attachment() {
        data class Meta(
            val durationSeconds: Double? = null,
            val audioCodec: String? = null,
            val audioBitrate: String? = null,
            val audioChannels: String? = null,
            val bitrate: Long? = null,
            val original: Meta? = null
        )
    }

    data class Unknown(
        override val attachmentId: String,
        override val url: String,
        override val previewUrl: String? = null,
        override val remoteUrl: String? = null,
        override val previewRemoteUrl: String? = null,
        override val textUrl: String? = null,
        override val description: String? = null,
        override val blurHash: String? = null,
    ) : Attachment()
}

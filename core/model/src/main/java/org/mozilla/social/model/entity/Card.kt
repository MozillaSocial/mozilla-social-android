package org.mozilla.social.model.entity

/**
 * Represents a rich preview card that is generated
 * using OpenGraph tags from a URL.
 */
sealed class Card {

    /**
     * URL of linked resource.
     */
    abstract val url: String

    /**
     * Title of linked resource.
     */
    abstract val title: String

    /**
     * Description of preview.
     */
    abstract val description: String

    /**
     * The author of the original resource.
     */
    abstract val authorName: String?

    /**
     * URL to the author of the original resource.
     */
    abstract val authorUrl: String?

    /**
     * The provider of the original resource.
     */
    abstract val providerName: String?

    /**
     * URL to the provider of the original resource.
     */
    abstract val providerUrl: String?

    /**
     * Width of preview, in pixels.
     */
    abstract val width: Long?

    /**
     * Height of preview, in pixels.
     */
    abstract val height: Long?

    /**
     * URL of a preview thumbnail.
     */
    abstract val image: String?

    /**
     * URL used for photo embeds, instead of custom HTML.
     */
    abstract val embedUrl: String?

    /**
     * A hash computed by the BlurHash algorithm.
     *
     * For generating colorful preview thumbnails when media
     * has not been downloaded yet.
     */
    abstract val blurHash: String?

    /**
     * HTML to be used for generating the preview card.
     */
    abstract val html: String?

    data class Video(
        override val url: String,
        override val title: String,
        override val description: String,
        override val authorName: String? = null,
        override val authorUrl: String? = null,
        override val providerName: String? = null,
        override val providerUrl: String? = null,
        override val html: String? = null,
        override val width: Long? = null,
        override val height: Long? = null,
        override val image: String? = null,
        override val embedUrl: String? = null,
        override val blurHash: String? = null
    ) : Card()

    data class Photo(
        override val url: String,
        override val title: String,
        override val description: String,
        override val authorName: String? = null,
        override val authorUrl: String? = null,
        override val providerName: String? = null,
        override val providerUrl: String? = null,
        override val html: String? = null,
        override val width: Long? = null,
        override val height: Long? = null,
        override val image: String? = null,
        override val embedUrl: String? = null,
        override val blurHash: String? = null
    ) : Card()

    data class Link(
        override val url: String,
        override val title: String,
        override val description: String,
        override val authorName: String? = null,
        override val authorUrl: String? = null,
        override val providerName: String? = null,
        override val providerUrl: String? = null,
        override val html: String? = null,
        override val width: Long? = null,
        override val height: Long? = null,
        override val image: String? = null,
        override val embedUrl: String? = null,
        override val blurHash: String? = null
    ) : Card()

    data class Rich(
        override val url: String,
        override val title: String,
        override val description: String,
        override val authorName: String? = null,
        override val authorUrl: String? = null,
        override val providerName: String? = null,
        override val providerUrl: String? = null,
        override val html: String? = null,
        override val width: Long? = null,
        override val height: Long? = null,
        override val image: String? = null,
        override val embedUrl: String? = null,
        override val blurHash: String? = null
    ) : Card()
}

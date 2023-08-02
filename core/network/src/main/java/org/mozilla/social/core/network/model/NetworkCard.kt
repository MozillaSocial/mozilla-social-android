package org.mozilla.social.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a rich preview card that is generated
 * using OpenGraph tags from a URL.
 */
@Serializable
sealed class NetworkCard {

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

    @Serializable
    @SerialName("video")
    data class Video(

        @SerialName("url")
        override val url: String,

        @SerialName("title")
        override val title: String,

        @SerialName("description")
        override val description: String,

        @SerialName("author_name")
        override val authorName: String? = null,

        @SerialName("author_url")
        override val authorUrl: String? = null,

        @SerialName("provider_name")
        override val providerName: String? = null,

        @SerialName("provider_url")
        override val providerUrl: String? = null,

        @SerialName("html")
        override val html: String? = null,

        @SerialName("width")
        override val width: Long? = null,

        @SerialName("height")
        override val height: Long? = null,

        @SerialName("image")
        override val image: String? = null,

        @SerialName("embed_url")
        override val embedUrl: String? = null,

        @SerialName("blurhash")
        override val blurHash: String? = null

    ) : NetworkCard()


    @Serializable
    @SerialName("photo")
    data class Photo(

        @SerialName("url")
        override val url: String,

        @SerialName("title")
        override val title: String,

        @SerialName("description")
        override val description: String,

        @SerialName("author_name")
        override val authorName: String? = null,

        @SerialName("author_url")
        override val authorUrl: String? = null,

        @SerialName("provider_name")
        override val providerName: String? = null,

        @SerialName("provider_url")
        override val providerUrl: String? = null,

        @SerialName("html")
        override val html: String? = null,

        @SerialName("width")
        override val width: Long? = null,

        @SerialName("height")
        override val height: Long? = null,

        @SerialName("image")
        override val image: String? = null,

        @SerialName("embed_url")
        override val embedUrl: String? = null,

        @SerialName("blurhash")
        override val blurHash: String? = null

    ) : NetworkCard()

    @Serializable
    @SerialName("link")
    data class Link(

        @SerialName("url")
        override val url: String,

        @SerialName("title")
        override val title: String,

        @SerialName("description")
        override val description: String,

        @SerialName("author_name")
        override val authorName: String? = null,

        @SerialName("author_url")
        override val authorUrl: String? = null,

        @SerialName("provider_name")
        override val providerName: String? = null,

        @SerialName("provider_url")
        override val providerUrl: String? = null,

        @SerialName("html")
        override val html: String? = null,

        @SerialName("width")
        override val width: Long? = null,

        @SerialName("height")
        override val height: Long? = null,

        @SerialName("image")
        override val image: String? = null,

        @SerialName("embed_url")
        override val embedUrl: String? = null,

        @SerialName("blurhash")
        override val blurHash: String? = null

    ) : NetworkCard()

    @Serializable
    @SerialName("rich")
    data class Rich(

        @SerialName("url")
        override val url: String,

        @SerialName("title")
        override val title: String,

        @SerialName("description")
        override val description: String,

        @SerialName("author_name")
        override val authorName: String? = null,

        @SerialName("author_url")
        override val authorUrl: String? = null,

        @SerialName("provider_name")
        override val providerName: String? = null,

        @SerialName("provider_url")
        override val providerUrl: String? = null,

        @SerialName("html")
        override val html: String? = null,

        @SerialName("width")
        override val width: Long? = null,

        @SerialName("height")
        override val height: Long? = null,

        @SerialName("image")
        override val image: String? = null,

        @SerialName("embed_url")
        override val embedUrl: String? = null,

        @SerialName("blurhash")
        override val blurHash: String? = null

    ) : NetworkCard()
}

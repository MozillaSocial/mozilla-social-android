package org.mozilla.social.core.network.mastodon.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a rich preview card that is generated
 * using OpenGraph tags from a URL.
 */
@Serializable
class NetworkCard(
    @SerialName("url")
    val url: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("type")
    val type: String,
    @SerialName("author_name")
    val authorName: String? = null,
    @SerialName("author_url")
    val authorUrl: String? = null,
    @SerialName("provider_name")
    val providerName: String? = null,
    @SerialName("provider_url")
    val providerUrl: String? = null,
    @SerialName("html")
    val html: String? = null,
    @SerialName("width")
    val width: Long? = null,
    @SerialName("height")
    val height: Long? = null,
    @SerialName("image")
    val image: String? = null,
    @SerialName("embed_url")
    val embedUrl: String? = null,
    @SerialName("blurhash")
    val blurHash: String? = null,
)

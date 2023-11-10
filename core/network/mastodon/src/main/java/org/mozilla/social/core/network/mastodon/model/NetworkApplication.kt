package org.mozilla.social.core.network.mastodon.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an application that interfaces with the
 * REST API to access accounts or post statuses.
 */
@Serializable
data class NetworkApplication(

    /**
     * The name of your application.
     */
    @SerialName("name")
    val name: String,

    /**
     * The website associated with your application.
     */
    @SerialName("website")
    val website: String? = null,

    /**
     * Used for Push Streaming API.
     *
     * Equivalent to [NetworkPushSubscription.serverKey].
     */
    @SerialName("vapid_key")
    val vapidKey: String? = null,

    /**
     * Client ID key, used for obtaining OAuth tokens.
     */
    @SerialName("client_id")
    val clientId: String? = null,

    /**
     * Client secret key, used for obtaining OAuth tokens.
     */
    @SerialName("client_secret")
    val clientSecret: String? = null,
)

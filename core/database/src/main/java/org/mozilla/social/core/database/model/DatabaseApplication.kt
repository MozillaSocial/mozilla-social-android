package org.mozilla.social.core.database.model

/**
 * Represents an application that interfaces with the
 * REST API to access accounts or post statuses.
 */
data class DatabaseApplication(

    /**
     * The name of your application.
     */
    val name: String,

    /**
     * The website associated with your application.
     */
    val website: String? = null,

    /**
     * Used for Push Streaming API.
     *
     * Equivalent to [PushSubscription.serverKey].
     */
    val vapidKey: String? = null,

    /**
     * Client ID key, used for obtaining OAuth tokens.
     */
    val clientId: String? = null,

    /**
     * Client secret key, used for obtaining OAuth tokens.
     */
    val clientSecret: String? = null,
)

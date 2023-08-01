package org.mozilla.social.core.network.model

import kotlinx.datetime.Instant

/**
 * Represents an OAuth token used for authenticating with the API and performing actions.
 */
data class NetworkToken(
    /**
     * An OAuth access token to be used for authorization.
     */
    val accessToken: String,

    /**
     * The OAuth token type. Mastodon uses `Bearer` tokens.
     */
    val tokenType: String,

    /**
     * The OAuth scopes granted by this token, space-separated.
     */
    val scope: String,

    /**
     * Time at which the token was generated.
     */
    val createdAt: Instant
)

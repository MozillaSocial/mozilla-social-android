package org.mozilla.social.core.model.request

import org.mozilla.social.core.model.Application

/**
 * Object to register a new [Application].
 */
data class ApplicationCreate(
    /**
     * A name for your application.
     */
    val clientName: String,
    /**
     * Where the user should be redirected after authorization.
     *
     * To display the authorization code to the user instead of
     * redirecting to a web page, use `urn:ietf:wg:oauth:2.0:oob` in this parameter.
     */
    val redirectUris: String,
    /**
     * Space-separated list of scopes.
     *
     * If none is provided, defaults to `read`.
     */
    val scopes: String?,
    /**
     * A URL to the homepage of your app.
     */
    val website: String?,
)

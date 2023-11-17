package org.mozilla.social.core.database.model

import kotlinx.serialization.Serializable

@Serializable
enum class DatabaseStatusVisibility(val value: String) {
    /**
     * Visible to everyone, shown in public timelines.
     */
    Public("public"),

    /**
     * Visible to public, but not included in public timelines.
     */
    Unlisted("unlisted"),

    /**
     * Visible to followers only, and to any mentioned users.
     */
    Private("private"),

    /**
     * Visible only to mentioned users.
     */
    Direct("direct"),
}

package org.mozilla.social.core.analytics

internal enum class EngagementType(
    val value: String,
) {
    FOLLOW("follow"),
    POST("post"),
    REPLY("reply"),
    FAVORITE("favorite"),
    BOOST("boost"),
    BOOKMARK("bookmark"),
    GENERAL("general"),
    SHARE("share"),
}

package org.mozilla.social.model

data class Recommendation(
    val url: String,
    val title: String,
    val imageUrl: String,
    val excerpt: String,
    val publisher: String,
    val timeToRead: String?
)
package org.mozilla.social.core.model

data class Recommendation(
    val id: String,
    val url: String,
    val title: String,
    val excerpt: String,
    val publisher: String,
    val image: List<RecommendationImage>,
    val authors: List<RecommendationAuthor>,
)

data class RecommendationImage(
    val url: String,
    val width: Int,
    val height: Int,
)

data class RecommendationAuthor(
    val name: String,
)
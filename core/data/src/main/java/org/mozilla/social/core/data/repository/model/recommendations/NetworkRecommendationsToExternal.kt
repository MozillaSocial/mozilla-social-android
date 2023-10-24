package org.mozilla.social.core.data.repository.model.recommendations

import org.mozilla.social.core.network.model.NetworkRecommendation
import org.mozilla.social.core.network.model.NetworkRecommendationAuthor
import org.mozilla.social.core.network.model.NetworkRecommendationImage
import org.mozilla.social.model.Recommendation
import org.mozilla.social.model.RecommendationAuthor
import org.mozilla.social.model.RecommendationImage

fun NetworkRecommendation.toExternalModel() = Recommendation(
    id = id,
    url = url,
    title = title,
    excerpt = excerpt,
    publisher = publisher,
    image = image.sizes.map { it.toExternalModel() },
    authors = authors.map { it.toExternalModel() }
)

fun NetworkRecommendationImage.toExternalModel() = RecommendationImage(
    url = url,
    width = width,
    height = height,
)

fun NetworkRecommendationAuthor.toExternalModel() = RecommendationAuthor(
    name = name,
)
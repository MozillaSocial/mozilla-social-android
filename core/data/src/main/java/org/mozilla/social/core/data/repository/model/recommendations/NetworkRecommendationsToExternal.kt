package org.mozilla.social.core.data.repository.model.recommendations

import org.mozilla.social.core.network.mozilla.model.NetworkRecommendation
import org.mozilla.social.core.network.mozilla.model.NetworkRecommendationAuthor
import org.mozilla.social.core.network.mozilla.model.NetworkRecommendationImage
import org.mozilla.social.model.Recommendation
import org.mozilla.social.model.RecommendationAuthor
import org.mozilla.social.model.RecommendationImage

fun org.mozilla.social.core.network.mozilla.model.NetworkRecommendation.toExternalModel() = Recommendation(
    id = id,
    url = url,
    title = title,
    excerpt = excerpt,
    publisher = publisher,
    image = image.sizes.map { it.toExternalModel() },
    authors = authors.map { it.toExternalModel() }
)

fun org.mozilla.social.core.network.mozilla.model.NetworkRecommendationImage.toExternalModel() = RecommendationImage(
    url = url,
    width = width,
    height = height,
)

fun org.mozilla.social.core.network.mozilla.model.NetworkRecommendationAuthor.toExternalModel() = RecommendationAuthor(
    name = name,
)
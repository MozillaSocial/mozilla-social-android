package org.mozilla.social.core.repository.mozilla.model

import org.mozilla.social.core.model.Recommendation
import org.mozilla.social.core.model.RecommendationAuthor
import org.mozilla.social.core.model.RecommendationImage
import org.mozilla.social.core.network.mozilla.model.NetworkRecommendation
import org.mozilla.social.core.network.mozilla.model.NetworkRecommendationAuthor
import org.mozilla.social.core.network.mozilla.model.NetworkRecommendationImage

fun NetworkRecommendation.toExternalModel() =
    Recommendation(
        id = id,
        url = url,
        title = title,
        excerpt = excerpt,
        publisher = publisher,
        image = image.sizes.map { it.toExternalModel() },
        authors = authors.map { it.toExternalModel() },
    )

fun NetworkRecommendationImage.toExternalModel() =
    RecommendationImage(
        url = url,
        width = width,
        height = height,
    )

fun NetworkRecommendationAuthor.toExternalModel() =
    RecommendationAuthor(
        name = name,
    )

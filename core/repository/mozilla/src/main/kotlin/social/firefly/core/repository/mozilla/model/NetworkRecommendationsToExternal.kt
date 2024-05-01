package social.firefly.core.repository.mozilla.model

import social.firefly.core.model.Recommendation
import social.firefly.core.model.RecommendationAuthor
import social.firefly.core.model.RecommendationImage
import social.firefly.core.network.mozilla.model.NetworkRecommendation
import social.firefly.core.network.mozilla.model.NetworkRecommendationAuthor
import social.firefly.core.network.mozilla.model.NetworkRecommendationImage

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

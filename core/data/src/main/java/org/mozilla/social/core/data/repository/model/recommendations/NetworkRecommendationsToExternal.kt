package org.mozilla.social.core.data.repository.model.recommendations

import org.mozilla.social.core.network.model.NetworkRecommendation
import org.mozilla.social.model.Recommendation

fun NetworkRecommendation.toExternalModel() = Recommendation(
    url = url,
    title = title,
    imageUrl = imageUrl,
    excerpt = excerpt,
    publisher = publisher,
    timeToRead = timeToRead?.let { "$it min read" }
)
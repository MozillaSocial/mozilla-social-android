package org.mozilla.social.core.repository.mastodon.model.instance

import org.mozilla.social.core.model.Instance
import org.mozilla.social.core.model.InstanceRule
import org.mozilla.social.core.model.InstanceStats
import org.mozilla.social.core.model.InstanceUrls
import org.mozilla.social.core.network.mastodon.model.NetworkInstance
import org.mozilla.social.core.network.mastodon.model.NetworkInstanceRule
import org.mozilla.social.core.network.mastodon.model.NetworkInstanceStats
import org.mozilla.social.core.network.mastodon.model.NetworkInstanceUrls
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel

fun NetworkInstanceRule.toExternalModel(): InstanceRule =
    InstanceRule(
        id = id,
        text = text,
    )

fun List<NetworkInstanceRule>.toExternalModel(): List<InstanceRule> =
    map { it.toExternalModel() }

fun NetworkInstance.toExternalModel(): Instance = Instance(
    uri = domain,
    title = title,
    description = description,
    version = version,
    languages = languages,
    thumbnail = thumbnail.url,
    contactAccount = contact?.account?.toExternalModel(),
    contactEmail = contact?.email,
)

fun NetworkInstanceUrls.toExternalModel(): InstanceUrls =
    InstanceUrls(streamingApiUrl = streamingApiUrl)

fun NetworkInstanceStats.toExternalModel() = InstanceStats(
    userCount = userCount,
    statusCount = statusCount,
    domainCount = domainCount
)

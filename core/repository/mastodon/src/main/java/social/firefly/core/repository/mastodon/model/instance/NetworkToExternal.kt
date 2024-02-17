package social.firefly.core.repository.mastodon.model.instance

import social.firefly.core.model.Instance
import social.firefly.core.model.InstanceRule
import social.firefly.core.model.InstanceStats
import social.firefly.core.model.InstanceUrls
import social.firefly.core.network.mastodon.model.NetworkInstance
import social.firefly.core.network.mastodon.model.NetworkInstanceRule
import social.firefly.core.network.mastodon.model.NetworkInstanceStats
import social.firefly.core.network.mastodon.model.NetworkInstanceUrls
import social.firefly.core.repository.mastodon.model.status.toExternalModel

fun NetworkInstanceRule.toExternalModel(): InstanceRule =
    InstanceRule(
        id = id,
        text = text,
    )

fun List<NetworkInstanceRule>.toExternalModel(): List<InstanceRule> = map { it.toExternalModel() }

fun NetworkInstance.toExternalModel(): Instance =
    Instance(
        uri = domain,
        title = title,
        description = description,
        version = version,
        languages = languages,
        thumbnail = thumbnail.url,
        contactAccount = contact?.account?.toExternalModel(),
        contactEmail = contact?.email,
        rules = rules.toExternalModel(),
    )

fun NetworkInstanceUrls.toExternalModel(): InstanceUrls = InstanceUrls(streamingApiUrl = streamingApiUrl)

fun NetworkInstanceStats.toExternalModel() =
    InstanceStats(
        userCount = userCount,
        statusCount = statusCount,
        domainCount = domainCount,
    )

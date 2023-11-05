package org.mozilla.social.core.data.repository.model.instance

import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.core.network.model.NetworkInstance
import org.mozilla.social.core.network.model.NetworkInstanceRule
import org.mozilla.social.core.network.model.NetworkInstanceStats
import org.mozilla.social.core.network.model.NetworkInstanceUrls
import org.mozilla.social.model.Instance
import org.mozilla.social.model.InstanceRule
import org.mozilla.social.model.InstanceStats
import org.mozilla.social.model.InstanceUrls

fun NetworkInstanceRule.toExternalModel(): InstanceRule =
    InstanceRule(
        id = id,
        text = text,
    )

fun List<NetworkInstanceRule>.toExternalModel(): List<InstanceRule> =
    map { it.toExternalModel() }

fun NetworkInstance.toExternalModel(): Instance = Instance(
    uri,
    title,
    description,
    shortDescription,
    email,
    version,
    languages,
    areRegistrationsEnabled,
    isApprovalRequired = isApprovalRequired,
    areInvitesEnabled = areInvitesEnabled,
    urls = urls.toExternalModel(),
    stats = stats.toExternalModel(),
    thumbnail = thumbnail,
    contactAccount = contactAccount?.toExternalModel()
)

fun NetworkInstanceUrls.toExternalModel(): InstanceUrls =
    InstanceUrls(streamingApiUrl = streamingApiUrl)

fun NetworkInstanceStats.toExternalModel() = InstanceStats(
    userCount = userCount,
    statusCount = statusCount,
    domainCount = domainCount
)
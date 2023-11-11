package org.mozilla.social.core.repository.mastodon.mappers

import org.mozilla.social.core.network.mastodon.model.request.NetworkMediaUpdate
import org.mozilla.social.core.network.mastodon.model.request.NetworkPollVote
import org.mozilla.social.core.network.mastodon.model.request.NetworkReportCreate
import org.mozilla.social.core.network.mastodon.model.request.NetworkStatusCreate
import org.mozilla.social.core.repository.mastodon.model.MediaUpdate
import org.mozilla.social.core.repository.mastodon.model.PollVote
import org.mozilla.social.core.repository.mastodon.model.status.toNetworkModel
import org.mozilla.social.model.request.ReportCreate
import org.mozilla.social.model.request.StatusCreate

internal fun PollVote.toNetworkModel(): NetworkPollVote = NetworkPollVote(choices = choices)

internal fun StatusCreate.toNetworkModel(): NetworkStatusCreate = NetworkStatusCreate(
    status = status,
    mediaIds = mediaIds,
    poll = poll?.toNetworkModel(),
    inReplyToId = inReplyToId,
    isSensitive = isSensitive,
    contentWarningText = contentWarningText,
    visibility = visibility?.toNetworkModel(),
    language = language
)

internal fun MediaUpdate.toNetworkModel(): NetworkMediaUpdate =
    NetworkMediaUpdate(description = description)

internal fun ReportCreate.toNetworkModel(): NetworkReportCreate = NetworkReportCreate(
    accountId = accountId,
    statusIds = statusIds,
    comment = comment,
    forward = forward,
    category = category, ruleViolations = ruleViolations,
)
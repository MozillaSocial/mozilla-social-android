package social.firefly.core.repository.mastodon.model.status

import social.firefly.core.model.MediaUpdate
import social.firefly.core.model.PollVote
import social.firefly.core.model.StatusVisibility
import social.firefly.core.model.request.PollCreate
import social.firefly.core.model.request.ReportCreate
import social.firefly.core.model.request.StatusCreate
import social.firefly.core.network.mastodon.model.responseBody.NetworkStatusVisibility
import social.firefly.core.network.mastodon.model.request.NetworkMediaUpdate
import social.firefly.core.network.mastodon.model.request.NetworkPollCreate
import social.firefly.core.network.mastodon.model.request.NetworkPollVote
import social.firefly.core.network.mastodon.model.request.NetworkReportCreate
import social.firefly.core.network.mastodon.model.request.NetworkStatusCreate

internal fun StatusVisibility.toNetworkModel(): social.firefly.core.network.mastodon.model.responseBody.NetworkStatusVisibility =
    when (this) {
        StatusVisibility.Direct -> social.firefly.core.network.mastodon.model.responseBody.NetworkStatusVisibility.Direct
        StatusVisibility.Private -> social.firefly.core.network.mastodon.model.responseBody.NetworkStatusVisibility.Private
        StatusVisibility.Public -> social.firefly.core.network.mastodon.model.responseBody.NetworkStatusVisibility.Public
        StatusVisibility.Unlisted -> social.firefly.core.network.mastodon.model.responseBody.NetworkStatusVisibility.Unlisted
    }

internal fun PollCreate.toNetworkModel(): NetworkPollCreate =
    NetworkPollCreate(
        options = options,
        expiresInSec = expiresInSec,
        allowMultipleChoices = allowMultipleChoices,
        hideTotals = hideTotals,
    )

internal fun PollVote.toNetworkModel(): NetworkPollVote = NetworkPollVote(choices = choices)

internal fun StatusCreate.toNetworkModel(): NetworkStatusCreate =
    NetworkStatusCreate(
        status = status,
        mediaIds = mediaIds,
        poll = poll?.toNetworkModel(),
        inReplyToId = inReplyToId,
        isSensitive = isSensitive,
        contentWarningText = contentWarningText,
        visibility = visibility?.toNetworkModel(),
        language = language,
    )

internal fun MediaUpdate.toNetworkModel(): NetworkMediaUpdate =
    NetworkMediaUpdate(description = description)

internal fun ReportCreate.toNetworkModel(): NetworkReportCreate =
    NetworkReportCreate(
        accountId = accountId,
        statusIds = statusIds,
        comment = comment,
        forward = forward,
        category = category,
        ruleViolations = ruleViolations,
    )

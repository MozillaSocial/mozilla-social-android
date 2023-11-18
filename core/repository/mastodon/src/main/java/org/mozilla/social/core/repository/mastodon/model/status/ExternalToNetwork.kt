package org.mozilla.social.core.repository.mastodon.model.status

import org.mozilla.social.core.model.MediaUpdate
import org.mozilla.social.core.model.PollVote
import org.mozilla.social.core.model.StatusVisibility
import org.mozilla.social.core.model.request.PollCreate
import org.mozilla.social.core.model.request.ReportCreate
import org.mozilla.social.core.model.request.StatusCreate
import org.mozilla.social.core.network.mastodon.model.NetworkStatusVisibility
import org.mozilla.social.core.network.mastodon.model.request.NetworkMediaUpdate
import org.mozilla.social.core.network.mastodon.model.request.NetworkPollCreate
import org.mozilla.social.core.network.mastodon.model.request.NetworkPollVote
import org.mozilla.social.core.network.mastodon.model.request.NetworkReportCreate
import org.mozilla.social.core.network.mastodon.model.request.NetworkStatusCreate

internal fun StatusVisibility.toNetworkModel(): NetworkStatusVisibility =
    when (this) {
        StatusVisibility.Direct -> NetworkStatusVisibility.Direct
        StatusVisibility.Private -> NetworkStatusVisibility.Private
        StatusVisibility.Public -> NetworkStatusVisibility.Public
        StatusVisibility.Unlisted -> NetworkStatusVisibility.Unlisted
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

internal fun MediaUpdate.toNetworkModel(): NetworkMediaUpdate = NetworkMediaUpdate(description = description)

internal fun ReportCreate.toNetworkModel(): NetworkReportCreate =
    NetworkReportCreate(
        accountId = accountId,
        statusIds = statusIds,
        comment = comment,
        forward = forward,
        category = category,
        ruleViolations = ruleViolations,
    )

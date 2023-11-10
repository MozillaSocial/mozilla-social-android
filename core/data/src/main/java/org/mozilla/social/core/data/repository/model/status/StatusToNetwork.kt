package org.mozilla.social.core.data.repository.model.status

import org.mozilla.social.core.network.mastodon.model.NetworkStatusVisibility
import org.mozilla.social.core.network.mastodon.model.request.NetworkPollCreate
import org.mozilla.social.model.StatusVisibility
import org.mozilla.social.model.request.PollCreate

fun StatusVisibility.toNetworkModel(): NetworkStatusVisibility =
    when(this) {
        StatusVisibility.Direct -> NetworkStatusVisibility.Direct
        StatusVisibility.Private -> NetworkStatusVisibility.Private
        StatusVisibility.Public -> NetworkStatusVisibility.Public
        StatusVisibility.Unlisted -> NetworkStatusVisibility.Unlisted
    }

fun PollCreate.toNetworkModel(): NetworkPollCreate =
    NetworkPollCreate(
        options = options,
        expiresInSec = expiresInSec,
        allowMultipleChoices = allowMultipleChoices,
        hideTotals = hideTotals
    )
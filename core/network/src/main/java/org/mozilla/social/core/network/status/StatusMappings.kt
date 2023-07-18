package org.mozilla.social.core.network.status

import org.mozilla.social.model.entity.PollOption
import org.mozilla.social.model.entity.StatusVisibility
import org.mozilla.social.model.entity.request.PollCreate
import org.mozilla.social.model.entity.request.StatusCreate

fun StatusCreate.toMastodonk(): fr.outadoc.mastodonk.api.entity.request.StatusCreate =
    fr.outadoc.mastodonk.api.entity.request.StatusCreate(
        status, mediaIds, poll?.toMastodonk(), inReplyToId, isSensitive, contentWarningText, visibility?.toMastodonk(), language
    )

fun PollCreate.toMastodonk(): fr.outadoc.mastodonk.api.entity.request.PollCreate =
    fr.outadoc.mastodonk.api.entity.request.PollCreate(
        options.map { it.toMastodonk() }, expiresInSec, allowMultipleChoices, hideTotals
    )

fun PollOption.toMastodonk(): fr.outadoc.mastodonk.api.entity.PollOption =
    fr.outadoc.mastodonk.api.entity.PollOption(
        title, votesCount
    )

fun StatusVisibility.toMastodonk(): fr.outadoc.mastodonk.api.entity.StatusVisibility =
    when (this) {
        StatusVisibility.Public -> fr.outadoc.mastodonk.api.entity.StatusVisibility.Public
        StatusVisibility.Unlisted -> fr.outadoc.mastodonk.api.entity.StatusVisibility.Unlisted
        StatusVisibility.Private -> fr.outadoc.mastodonk.api.entity.StatusVisibility.Private
        StatusVisibility.Direct -> fr.outadoc.mastodonk.api.entity.StatusVisibility.Direct
    }
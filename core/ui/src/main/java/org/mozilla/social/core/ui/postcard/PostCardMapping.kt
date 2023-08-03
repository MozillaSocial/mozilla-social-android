package org.mozilla.social.core.ui.postcard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Reply
import androidx.core.text.HtmlCompat
import org.mozilla.social.common.utils.timeSinceNow
import org.mozilla.social.core.ui.poll.toPollUiState
import org.mozilla.social.model.Post
import org.mozilla.social.model.Status

fun Post.toPostCardUiState(): PostCardUiState =
    PostCardUiState(
        topRowMetaDataUiState = toTopRowMetaDataUiState(),
        mainPostCardUiState = status.boostedStatus?.toMainPostCardUiState() ?: status.toMainPostCardUiState()
    )

private fun Status.toMainPostCardUiState(): MainPostCardUiState =
    MainPostCardUiState(
        pollUiState = poll?.toPollUiState(),
        statusText = HtmlCompat.fromHtml(content, 0),
        mediaAttachments = mediaAttachments,
        profilePictureUrl = account.avatarStaticUrl,
        postMetaDataText = "${createdAt.timeSinceNow()} - @${account.acct}",
        replyCount = repliesCount,
        boostCount = boostsCount,
        favoriteCount = favouritesCount,
        username = account.username,
        statusId = statusId,
    )

private fun Post.toTopRowMetaDataUiState(): TopRowMetaDataUiState =
    TopRowMetaDataUiState(
        text = if (status.boostedStatus != null) {
            "${status.account.username} boosted"
        } else if (inReplyToAccountName != null) {
            "In reply to $inReplyToAccountName"
        } else "",
        icon = if (status.boostedStatus != null) {
            Icons.Default.Repeat
        } else if (inReplyToAccountName != null) {
            Icons.Default.Reply
        } else Icons.Default.Reply,
    )
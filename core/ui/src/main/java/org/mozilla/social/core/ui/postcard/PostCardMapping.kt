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

private fun Post.toTopRowMetaDataUiState(): TopRowMetaDataUiState? =
    if (status.boostedStatus != null) {
        TopRowMetaDataUiState(
            text = "${status.account.username} boosted",
            icon = Icons.Default.Repeat
        )
    } else if (inReplyToAccountName != null) {
        TopRowMetaDataUiState(
            text = "In reply to $inReplyToAccountName",
            icon = Icons.Default.Reply
        )
    } else null

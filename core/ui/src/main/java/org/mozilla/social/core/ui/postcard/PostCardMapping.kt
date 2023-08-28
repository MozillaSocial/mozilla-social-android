package org.mozilla.social.core.ui.postcard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Reply
import androidx.core.text.HtmlCompat
import org.mozilla.social.common.utils.timeSinceNow
import org.mozilla.social.core.ui.poll.toPollUiState
import org.mozilla.social.model.Post
import org.mozilla.social.model.Status

/**
 * @param currentUserAccountId refers to the current user, not necessarily the creator of the Status.
 */
fun Status.toPostCardUiState(
    currentUserAccountId: String,
): PostCardUiState =
    PostCardUiState(
        statusId = statusId,
        topRowMetaDataUiState = toTopRowMetaDataUiState(),
        mainPostCardUiState = boostedStatus?.toMainPostCardUiState(currentUserAccountId)
            ?: toMainPostCardUiState(currentUserAccountId)
    )

private fun Status.toMainPostCardUiState(
    currentUserAccountId: String,
): MainPostCardUiState =
    MainPostCardUiState(
        url = url,
        pollUiState = poll?.toPollUiState(
            isUserCreatedPoll = currentUserAccountId == account.accountId
        ),
        statusText = HtmlCompat.fromHtml(content, 0),
        mediaAttachments = mediaAttachments,
        profilePictureUrl = account.avatarStaticUrl,
        postMetaDataText = "${createdAt.timeSinceNow()} - @${account.acct}",
        replyCount = repliesCount,
        boostCount = boostsCount,
        favoriteCount = favouritesCount,
        username = account.username,
        statusId = statusId,
        userBoosted = isBoosted ?: false
    )

private fun Status.toTopRowMetaDataUiState(): TopRowMetaDataUiState? =
    if (boostedStatus != null) {
        TopRowMetaDataUiState(
            text = "${account.username} boosted",
            icon = Icons.Default.Repeat
        )
    } else if (inReplyToAccountName != null) {
        TopRowMetaDataUiState(
            text = "In reply to $inReplyToAccountName",
            icon = Icons.Default.Reply
        )
    } else null

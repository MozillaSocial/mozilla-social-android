package org.mozilla.social.core.ui.postcard

import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.common.utils.timeSinceNow
import org.mozilla.social.core.model.Card
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.ui.common.R
import org.mozilla.social.core.ui.poll.toPollUiState

/**
 * @param currentUserAccountId refers to the current user, not necessarily the creator of the Status.
 */
fun Status.toPostCardUiState(currentUserAccountId: String): PostCardUiState =
    PostCardUiState(
        statusId = statusId,
        topRowMetaDataUiState = toTopRowMetaDataUiState(),
        mainPostCardUiState =
            boostedStatus?.toMainPostCardUiState(currentUserAccountId)
                ?: toMainPostCardUiState(currentUserAccountId),
    )

private fun Status.toMainPostCardUiState(currentUserAccountId: String): MainPostCardUiState =
    MainPostCardUiState(
        url = url,
        profilePictureUrl = account.avatarStaticUrl,
        postTimeSince = createdAt.timeSinceNow(),
        accountName = StringFactory.literal(account.acct),
        replyCount = repliesCount,
        boostCount = boostsCount,
        favoriteCount = favouritesCount,
        username = account.displayName,
        statusId = statusId,
        userBoosted = isBoosted ?: false,
        isFavorited = isFavourited ?: false,
        accountId = account.accountId,
        isUsersPost = currentUserAccountId == account.accountId,
        isBeingDeleted = isBeingDeleted,
        postContentUiState = toPostContentUiState(currentUserAccountId)
    )

private fun Status.toPostContentUiState(currentUserAccountId: String): PostContentUiState = PostContentUiState(
    statusId = statusId,
    pollUiState = poll?.toPollUiState(
        isUserCreatedPoll = currentUserAccountId == account.accountId,
    ),
    statusTextHtml = content,
    mediaAttachments = mediaAttachments,
    mentions = mentions,
    previewCard = card?.toPreviewCard(),
    contentWarning = contentWarningText,
)

private fun Status.toTopRowMetaDataUiState(): TopRowMetaDataUiState? =
    if (boostedStatus != null) {
        TopRowMetaDataUiState(
            text = StringFactory.resource(R.string.user_has_boosted_post, account.username),
            iconType = TopRowIconType.BOOSTED,
        )
    } else if (inReplyToAccountName != null) {
        TopRowMetaDataUiState(
            text = StringFactory.resource(R.string.post_is_in_reply_to_user, inReplyToAccountName!!),
            iconType = TopRowIconType.REPLY,
        )
    } else {
        null
    }

private fun Card.toPreviewCard(): PreviewCard? =
    image?.let {
        PreviewCard(
            url = url,
            title = title,
            imageUrl = it,
            providerName = providerName,
        )
    }

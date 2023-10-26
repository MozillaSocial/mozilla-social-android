package org.mozilla.social.core.ui.postcard

import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.common.utils.timeSinceNow
import org.mozilla.social.core.ui.R
import org.mozilla.social.core.ui.poll.toPollUiState
import org.mozilla.social.model.Card
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
        statusTextHtml = content,
        mediaAttachments = mediaAttachments,
        profilePictureUrl = account.avatarStaticUrl,
        postTimeSince = createdAt.timeSinceNow(),
        accountName = StringFactory.literal(account.acct),
        replyCount = repliesCount,
        boostCount = boostsCount,
        favoriteCount = favouritesCount,
        username = account.username,
        statusId = statusId,
        userBoosted = isBoosted ?: false,
        isFavorited = isFavourited ?: false,
        accountId = account.accountId,
        mentions = mentions,
        previewCard = card?.toPreviewCard(),
    )

private fun Status.toTopRowMetaDataUiState(): TopRowMetaDataUiState? =
    if (boostedStatus != null) {
        TopRowMetaDataUiState(
            text = StringFactory.resource(R.string.user_has_boosted_post, account.username),
            iconType = TopRowIconType.BOOSTED
        )
    } else if (inReplyToAccountName != null) {
        TopRowMetaDataUiState(
            text = StringFactory.resource(R.string.post_is_in_reply_to_user, inReplyToAccountName!!),
            iconType = TopRowIconType.REPLY
        )
    } else null

private fun Card.toPreviewCard(): PreviewCard =
    PreviewCard(
        url = url,
        title = title,
        imageUrl = image,
        providerName = providerName,
    )
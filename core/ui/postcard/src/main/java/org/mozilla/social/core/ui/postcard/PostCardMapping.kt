package org.mozilla.social.core.ui.postcard

import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.common.utils.timeSinceNow
import org.mozilla.social.common.utils.toShortenedStringValue
import org.mozilla.social.core.model.Card
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.ui.common.R
import org.mozilla.social.core.ui.poll.toPollUiState

/**
 * @param currentUserAccountId refers to the current user, not necessarily the creator of the Status.
 */
fun Status.toPostCardUiState(
    currentUserAccountId: String,
    postCardInteractions: PostCardInteractions
): PostCardUiState =
    PostCardUiState(
        statusId = statusId,
        topRowMetaDataUiState = toTopRowMetaDataUiState(),
        mainPostCardUiState =
        boostedStatus?.toMainPostCardUiState(currentUserAccountId, postCardInteractions)
            ?: toMainPostCardUiState(currentUserAccountId, postCardInteractions),
    )

private fun Status.toMainPostCardUiState(
    currentUserAccountId: String,
    postCardInteractions: PostCardInteractions
): MainPostCardUiState =
    MainPostCardUiState(
        url = url,
        profilePictureUrl = account.avatarStaticUrl,
        postTimeSince = createdAt.timeSinceNow(),
        accountName = StringFactory.literal(account.acct),
        replyCount = repliesCount.toShortenedStringValue(),
        boostCount = boostsCount.toShortenedStringValue(),
        favoriteCount = favouritesCount.toShortenedStringValue(),
        username = account.displayName,
        statusId = statusId,
        userBoosted = isBoosted ?: false,
        isFavorited = isFavourited ?: false,
        accountId = account.accountId,
        isBeingDeleted = isBeingDeleted,
        postContentUiState = toPostContentUiState(currentUserAccountId),
        dropDownOptions = toDropDownOptions(
            isUsersPost = currentUserAccountId == account.accountId,
            postCardInteractions = postCardInteractions
        )
    )

fun Status.toDropDownOptions(
    isUsersPost: Boolean,
    postCardInteractions: PostCardInteractions
): List<DropDownOption> {
    return buildList {
        if (isUsersPost) {
            add(
                DropDownOption(
                    text = StringFactory.resource(resId = R.string.delete_post),
                    onOptionClicked = { postCardInteractions.onOverflowDeleteClicked(statusId) }
                )
            )
//            add(
//                DropDownOption(
//                    text = StringFactory.resource(resId = org.mozilla.social.core.ui.postcard.R.string.edit_post),
//                    onOptionClicked = { postCardInteractions.onOverflowEditClicked(statusId) },
//                )
//            )
        } else {
            add(
                DropDownOption(
                    text = StringFactory.resource(
                        R.string.mute_user,
                        account.displayName
                    ),
                    onOptionClicked = {
                        postCardInteractions.onOverflowMuteClicked(
                            accountId = account.accountId,
                            statusId = statusId,
                        )
                    },
                )
            )

            add(
                DropDownOption(
                    text = StringFactory.resource(
                        R.string.block_user,
                        account.displayName
                    ),
                    onOptionClicked = {
                        postCardInteractions.onOverflowBlockClicked(
                            accountId = account.accountId,
                            statusId = statusId,
                        )
                    },
                )
            )
            add(
                DropDownOption(
                    text = StringFactory.resource(
                        R.string.report_user,
                        account.displayName
                    ),
                    onOptionClicked = {
                        postCardInteractions.onOverflowReportClicked(
                            accountId = account.accountId,
                            accountHandle = account.acct,
                            statusId = statusId,
                        )
                    }
                ),
            )
        }
    }
}

fun Status.toPostContentUiState(
    currentUserAccountId: String,
    contentWarningOverride: String? = null,
    onlyShowPreviewOfText: Boolean = false,
): PostContentUiState = PostContentUiState(
    pollUiState = poll?.toPollUiState(
        isUserCreatedPoll = currentUserAccountId == account.accountId,
    ),
    statusTextHtml = content,
    mediaAttachments = mediaAttachments,
    mentions = mentions,
    previewCard = card?.toPreviewCard(),
    contentWarning = contentWarningOverride ?: contentWarningText,
    onlyShowPreviewOfText = onlyShowPreviewOfText,
)

private fun Status.toTopRowMetaDataUiState(): TopRowMetaDataUiState? =
    if (boostedStatus != null) {
        TopRowMetaDataUiState(
            text = StringFactory.resource(R.string.user_has_reposted_post, account.username),
            iconType = TopRowIconType.BOOSTED,
        )
    } else if (inReplyToAccountName != null) {
        TopRowMetaDataUiState(
            text = StringFactory.resource(
                R.string.post_is_in_reply_to_user,
                inReplyToAccountName!!
            ),
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

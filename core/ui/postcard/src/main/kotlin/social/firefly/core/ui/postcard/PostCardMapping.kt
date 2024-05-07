package social.firefly.core.ui.postcard

import social.firefly.common.utils.StringFactory
import social.firefly.common.utils.timeSinceNow
import social.firefly.common.utils.toShortenedStringValue
import social.firefly.core.model.Card
import social.firefly.core.model.Status
import social.firefly.core.ui.common.R
import social.firefly.core.ui.poll.toPollUiState

/**
 * @param currentUserAccountId refers to the current user, not necessarily the creator of the Status.
 */
fun Status.toPostCardUiState(
    currentUserAccountId: String,
    depthLinesUiState: DepthLinesUiState? = null,
    showTopRowMetaData: Boolean = true,
    isClickable: Boolean = true,
    shouldShowUnfavoriteConfirmation: Boolean = false,
    shouldShowUnbookmarkConfirmation: Boolean = false,
): PostCardUiState =
    PostCardUiState(
        statusId = statusId,
        topRowMetaDataUiState = if (showTopRowMetaData) {
            toTopRowMetaDataUiState()
        } else {
            null
        },
        mainPostCardUiState = boostedStatus?.toMainPostCardUiState(
            currentUserAccountId = currentUserAccountId,
            shouldShowUnbookmarkConfirmation = shouldShowUnbookmarkConfirmation,
            shouldShowUnfavoriteConfirmation = shouldShowUnfavoriteConfirmation,
        ) ?: toMainPostCardUiState(
            currentUserAccountId = currentUserAccountId,
            shouldShowUnbookmarkConfirmation = shouldShowUnbookmarkConfirmation,
            shouldShowUnfavoriteConfirmation = shouldShowUnfavoriteConfirmation,
        ),
        depthLinesUiState = depthLinesUiState,
        isClickable = isClickable,
    )

private fun Status.toMainPostCardUiState(
    currentUserAccountId: String,
    shouldShowUnfavoriteConfirmation: Boolean = false,
    shouldShowUnbookmarkConfirmation: Boolean = false,
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
        isBookmarked = isBookmarked ?: false,
        accountId = account.accountId,
        isBeingDeleted = isBeingDeleted,
        postContentUiState = toPostContentUiState(
            statusId = statusId,
            currentUserAccountId = currentUserAccountId
        ),
        overflowDropDownType = when {
            currentUserAccountId == account.accountId -> OverflowDropDownType.USER
            else -> OverflowDropDownType.NOT_USER
        },
        shouldShowUnbookmarkConfirmation = shouldShowUnbookmarkConfirmation,
        shouldShowUnfavoriteConfirmation = shouldShowUnfavoriteConfirmation,
    )

fun Status.toDropDownOptions(
    isUsersPost: Boolean,
    postCardInteractions: PostCardInteractions
): List<DropDownOption> {
    return buildList {
        if (isUsersPost) {
            add(
                DropDownOption(
                    text = StringFactory.resource(resId = social.firefly.core.ui.postcard.R.string.edit_post),
                    onOptionClicked = { postCardInteractions.onOverflowEditClicked(statusId) },
                )
            )
            add(
                DropDownOption(
                    text = StringFactory.resource(resId = R.string.delete_post),
                    onOptionClicked = { postCardInteractions.onOverflowDeleteClicked(statusId) }
                )
            )
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
    statusId: String,
    currentUserAccountId: String,
    contentWarningOverride: String? = null,
    onlyShowPreviewOfText: Boolean = false,
): PostContentUiState = PostContentUiState(
    statusId = statusId,
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

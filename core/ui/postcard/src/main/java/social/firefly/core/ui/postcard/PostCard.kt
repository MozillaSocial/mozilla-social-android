package social.firefly.core.ui.postcard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.datetime.Instant
import social.firefly.common.utils.StringFactory
import social.firefly.common.utils.timeSinceNow
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfRadius
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.designsystem.utils.NoRipple
import social.firefly.core.ui.common.TransparentNoTouchOverlay
import social.firefly.core.ui.common.dropdown.FfDropDownItem
import social.firefly.core.ui.common.dropdown.FfDropdownMenu
import social.firefly.core.ui.common.loading.FfCircularProgressIndicator
import social.firefly.core.ui.common.media.MediaDisplay
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.core.ui.common.utils.getMaxWidth
import social.firefly.core.ui.common.utils.shareUrl
import social.firefly.core.ui.htmlcontent.HtmlContent
import social.firefly.core.ui.poll.Poll

/**
 * @param threadId if viewing this post from a thread, pass the threadId in to prevent
 * the user from being able to click on the same status as the thread's root status
 */
@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    post: PostCardUiState,
    postCardInteractions: PostCardInteractions,
    threadId: String? = null,
) {
    NoRipple {
        Box(modifier = modifier) {
            Column(
                Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable {
                        // prevent the user from being able to click on the same status
                        // as the root thread status
                        if (post.mainPostCardUiState.statusId != threadId) {
                            postCardInteractions.onPostCardClicked(post.mainPostCardUiState.statusId)
                        }
                    },
            ) {
                post.topRowMetaDataUiState?.let {
                    TopRowMetaData(
                        topRowMetaDataUiState = it,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Post(post.mainPostCardUiState, postCardInteractions)
            }

            AnimatedVisibility(
                modifier = Modifier.matchParentSize(),
                visible = post.mainPostCardUiState.isBeingDeleted,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                TransparentNoTouchOverlay()
            }
        }
    }
}

@Composable
private fun TopRowMetaData(
    modifier: Modifier = Modifier,
    topRowMetaDataUiState: TopRowMetaDataUiState,
) {
    Row(
        modifier = modifier,
    ) {
        Icon(
            modifier =
            Modifier
                .size(20.dp)
                .align(Alignment.CenterVertically),
            painter =
            when (topRowMetaDataUiState.iconType) {
                TopRowIconType.BOOSTED -> FfIcons.boost()
                TopRowIconType.REPLY -> FfIcons.chatBubbles()
            },
            contentDescription = "",
        )
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = topRowMetaDataUiState.text.build(LocalContext.current),
            style = FfTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun Post(
    post: MainPostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    Row {
        Avatar(post = post, postCardInteractions = postCardInteractions)
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Column {
            MetaData(
                post = post,
            )

            PostContent(
                uiState = post.postContentUiState,
                postCardInteractions = postCardInteractions,
            )

            BottomRow(
                modifier = Modifier,
                post = post,
                postCardInteractions = postCardInteractions,
            )
        }
    }
}

@Composable
private fun Avatar(
    modifier: Modifier = Modifier,
    post: MainPostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    AsyncImage(
        modifier =
        modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(FfTheme.colors.layer2)
            .clickable { postCardInteractions.onAccountImageClicked(post.accountId) },
        model = post.profilePictureUrl,
        contentDescription = null,
    )
}

@Composable
private fun MetaData(
    modifier: Modifier = Modifier,
    post: MainPostCardUiState,
) {
    val context = LocalContext.current

    Row(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = post.username,
                style = FfTheme.typography.labelMedium,
            )
            Text(
                text = "${post.postTimeSince.build(context)} - @${post.accountName.build(context)}",
                style = FfTheme.typography.bodyMedium,
                color = FfTheme.colors.textSecondary,
            )
        }
        OverflowMenu(
            post = post,
        )
    }
}

@Composable
fun PostContent(
    uiState: PostContentUiState,
    postCardInteractions: PostCardInteractions,
    modifier: Modifier = Modifier,
) {
    ContentWarning(
        modifier = modifier,
        contentWarningText = uiState.contentWarning,
    ) {
        Column {
            HtmlContent(
                mentions = uiState.mentions,
                htmlText = uiState.statusTextHtml,
                htmlContentInteractions = postCardInteractions,
                maximumLineCount = if (uiState.onlyShowPreviewOfText) {
                    1
                } else {
                    Int.MAX_VALUE
                },
                textColor = if (uiState.onlyShowPreviewOfText) {
                    FfTheme.colors.textSecondary
                } else {
                    FfTheme.colors.textPrimary
                }
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))

            MediaDisplay(
                attachments = uiState.mediaAttachments,
                onAttachmentClicked = {
                    postCardInteractions.onMediaClicked(
                        uiState.mediaAttachments,
                        uiState.mediaAttachments.indexOf(it),
                    )
                }
            )

            uiState.pollUiState?.let { Poll(it, postCardInteractions) }

            // only display preview card if there are no other media attachments
            if (uiState.previewCard != null && uiState.mediaAttachments.isEmpty()) {
                PreviewCard(
                    previewCard = uiState.previewCard,
                    htmlContentInteractions = postCardInteractions,
                )
            }
        }
    }
}

@Composable
private fun ContentWarning(
    modifier: Modifier = Modifier,
    contentWarningText: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        val hasContentWarning by remember(contentWarningText) {
            derivedStateOf { contentWarningText.isNotBlank() }
        }
        var isShowing by remember(hasContentWarning) {
            mutableStateOf(!hasContentWarning)
        }

        if (hasContentWarning) {
            Column(
                modifier =
                Modifier
                    .clickable { isShowing = !isShowing },
            ) {
                Row(
                    modifier =
                    Modifier
                        .clip(RoundedCornerShape(FfRadius.sm_4_dp))
                        .background(FfTheme.colors.layerActionWarning)
                        .padding(horizontal = FfSpacing.sm, vertical = FfSpacing.xs),
                ) {
                    Icon(
                        modifier =
                        Modifier
                            .size(14.dp)
                            .align(Alignment.CenterVertically),
                        painter = FfIcons.warning(),
                        contentDescription = "",
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        modifier =
                        Modifier
                            .align(Alignment.CenterVertically),
                        text = contentWarningText,
                        style = FfTheme.typography.labelSmall,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text(
                        modifier =
                        Modifier
                            .align(Alignment.CenterVertically)
                            .sizeIn(minWidth = 80.dp),
                        text =
                        if (isShowing) {
                            stringResource(id = R.string.hide_post)
                        } else {
                            stringResource(id = R.string.show_post)
                        },
                        style = FfTheme.typography.bodyMedium,
                        textDecoration = TextDecoration.Underline,
                    )

                    val rotatedDegrees = 90f
                    val rotation: Float by animateFloatAsState(
                        targetValue = if (isShowing) rotatedDegrees else 0f,
                        animationSpec = tween(),
                        label = "",
                    )
                    Icon(
                        modifier =
                        Modifier
                            .rotate(rotation)
                            .align(Alignment.CenterVertically),
                        painter = FfIcons.caretRight(),
                        contentDescription = null,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        AnimatedVisibility(visible = isShowing) {
            content()
        }
    }
}

@Composable
private fun OverflowMenu(
    post: MainPostCardUiState,
) {
    val overflowMenuExpanded = remember { mutableStateOf(false) }
    val context = LocalContext.current

    IconButton(
        modifier = Modifier.width(IntrinsicSize.Max),
        onClick = { overflowMenuExpanded.value = true },
    ) {
        if (post.isBeingDeleted) {
            FfCircularProgressIndicator(
                modifier =
                Modifier
                    .size(26.dp),
            )
        } else {
            Icon(painter = FfIcons.moreVertical(), contentDescription = "")
        }

        FfDropdownMenu(
            expanded = overflowMenuExpanded.value,
            onDismissRequest = {
                overflowMenuExpanded.value = false
            },
        ) {
            for (dropDownOption in post.dropDownOptions) {
                FfDropDownItem(
                    text = dropDownOption.text.build(context),
                    expanded = overflowMenuExpanded,
                    onClick = dropDownOption.onOptionClicked,
                )
            }
        }
    }
}

@Composable
private fun BottomRow(
    modifier: Modifier = Modifier,
    post: MainPostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    val context = LocalContext.current

    Row(
        modifier =
        modifier
            .requiredWidth(getMaxWidth() + 20.dp),
    ) {
        BottomIconButton(
            modifier = Modifier.weight(1f),
            onClick = { postCardInteractions.onReplyClicked(post.statusId) },
            painter = FfIcons.chatBubbles(),
            count = post.replyCount,
        )
        BottomIconButton(
            modifier = Modifier.weight(1f),
            onClick = { postCardInteractions.onBoostClicked(post.statusId, !post.userBoosted) },
            painter = FfIcons.boost(),
            count = post.boostCount,
            highlighted = post.userBoosted,
        )
        BottomIconButton(
            modifier = Modifier.weight(1f),
            onClick = { postCardInteractions.onFavoriteClicked(post.statusId, !post.isFavorited) },
            painter = if (post.isFavorited) FfIcons.heartFilled() else FfIcons.heart(),
            count = post.favoriteCount,
            highlighted = post.isFavorited,
            highlightColor = FfTheme.colors.textWarning,
        )
        BottomIconButton(
            onClick = {
                post.url?.let { url ->
                    shareUrl(url, context)
                }
            },
            painter = FfIcons.share(),
        )
    }
}

@Composable
private fun BottomIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    painter: Painter,
    count: String? = null,
    highlighted: Boolean = false,
    highlightColor: Color = FfTheme.colors.iconAccent,
) {
    Row(
        modifier = modifier,
    ) {
        IconButton(
            modifier =
            Modifier
                .width(IntrinsicSize.Max)
                .align(Alignment.CenterVertically),
            onClick = onClick,
        ) {
            Icon(
                painter = painter,
                contentDescription = "",
                tint =
                if (highlighted) {
                    highlightColor
                } else {
                    LocalContentColor.current
                },
            )
        }
        if (count != null) {
            Text(
                modifier =
                Modifier
                    .align(Alignment.CenterVertically)
                    .offset(x = -6.dp),
                text = count,
            )
        }
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun PostCardPreview() {
    PreviewTheme {
        PostCard(
            post =
            PostCardUiState(
                statusId = "",
                topRowMetaDataUiState =
                TopRowMetaDataUiState(
                    TopRowIconType.REPLY,
                    StringFactory.literal("in reply to Other person"),
                ),
                mainPostCardUiState =
                MainPostCardUiState(
                    url = "",
                    username = "Cool guy",
                    profilePictureUrl = "",
                    postTimeSince = Instant.fromEpochMilliseconds(1695308821000L).timeSinceNow(),
                    accountName = StringFactory.literal("coolguy"),
                    replyCount = "4",
                    boostCount = "300k",
                    favoriteCount = "4.4m",
                    statusId = "",
                    userBoosted = false,
                    isFavorited = false,
                    accountId = "",
                    isBeingDeleted = false,
                    postContentUiState = PostContentUiState(
                        pollUiState = null,
                        statusTextHtml = "<p><span class=\"h-card\"><a href=\"https://mozilla.social/@obez\" class=\"u-url mention\" rel=\"nofollow noopener noreferrer\" target=\"_blank\">@<span>obez</span></a></span> This is a text status.  Here is the text and that is all I have to say about that.</p>",
                        mediaAttachments = emptyList(),
                        mentions = emptyList(),
                        previewCard = null,
                        contentWarning = "",
                    ),
                    dropDownOptions = listOf(),
                ),
            ),
            postCardInteractions = object : PostCardInteractions {},
        )
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun PostCardWithContentWarningPreview() {
    PreviewTheme {
        PostCard(
            post =
            PostCardUiState(
                statusId = "",
                topRowMetaDataUiState =
                TopRowMetaDataUiState(
                    TopRowIconType.REPLY,
                    StringFactory.literal("in reply to Other person"),
                ),
                mainPostCardUiState =
                MainPostCardUiState(
                    url = "",
                    username = "Cool guy",
                    profilePictureUrl = "",
                    postTimeSince = Instant.fromEpochMilliseconds(1695308821000L).timeSinceNow(),
                    accountName = StringFactory.literal("coolguy"),
                    replyCount = "4",
                    boostCount = "300k",
                    favoriteCount = "4.4m",
                    statusId = "",
                    userBoosted = false,
                    isFavorited = false,
                    accountId = "",
                    isBeingDeleted = false,
                    postContentUiState = PostContentUiState(
                        pollUiState = null,
                        statusTextHtml = "<p><span class=\"h-card\"><a href=\"https://mozilla.social/@obez\" class=\"u-url mention\" rel=\"nofollow noopener noreferrer\" target=\"_blank\">@<span>obez</span></a></span> This is a text status.  Here is the text and that is all I have to say about that.</p>",
                        mediaAttachments = emptyList(),
                        mentions = emptyList(),
                        previewCard = null,
                        contentWarning = "Micky mouse spoilers!",
                    ),
                    dropDownOptions = listOf(),
                ),
            ),
            postCardInteractions = object : PostCardInteractions {},
        )
    }
}

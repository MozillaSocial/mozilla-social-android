package social.firefly.core.ui.postcard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import social.firefly.common.utils.StringFactory
import social.firefly.core.designsystem.utils.NoRipple
import social.firefly.core.ui.common.TransparentNoTouchOverlay
import social.firefly.core.ui.common.text.MediumTextLabel
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.core.ui.postcard.components.Avatar
import social.firefly.core.ui.postcard.components.BottomRow
import social.firefly.core.ui.postcard.components.DepthLines
import social.firefly.core.ui.postcard.components.DepthLinesExpandButton
import social.firefly.core.ui.postcard.components.MetaData
import social.firefly.core.ui.postcard.components.PostContent
import social.firefly.core.ui.postcard.components.TopRowMetaData

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    post: PostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    NoRipple {
        Box(modifier = modifier) {
            Layout(
                content = {
                    DepthLines(
                        depthLinesUiState = post.depthLinesUiState
                    )
                    DepthLinesExpandButton(
                        postCardUiState = post,
                        postCardInteractions = postCardInteractions,
                    )
                    Column(
                        modifier = Modifier
                            .padding(end = 8.dp, bottom = 8.dp, top = 8.dp)
                            .clickable {
                                if (post.isClickable) {
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
                        Post(
                            post = post.mainPostCardUiState,
                            showViewMoreReplies = post.depthLinesUiState?.showViewMoreRepliesText
                                ?: false,
                            postCardInteractions = postCardInteractions,
                        )
                    }
                },
            ) {
                (depthLinesMeasureable,
                    depthLinesExpandButtonMeasureable,
                    postContentMeasureable),
                constraints ->

                val postContentPlaceable = postContentMeasureable.measure(
                    constraints.copy(
                        maxWidth = constraints.maxWidth - depthLinesMeasureable.minIntrinsicWidth(0)
                    )
                )
                val depthLinesPlaceable = depthLinesMeasureable.measure(
                    constraints.copy(
                        minHeight = postContentPlaceable.height,
                        maxHeight = postContentPlaceable.height,
                    )
                )
                val depthLinesExpandButtonPlaceable = depthLinesExpandButtonMeasureable.measure(constraints)
                layout(
                    width = constraints.maxWidth,
                    height = postContentPlaceable.height,
                ) {
                    depthLinesPlaceable.place(
                        x = 0,
                        y = 0,
                    )
                    postContentPlaceable.place(
                        x = depthLinesPlaceable.width,
                        y = 0,
                    )
                    depthLinesExpandButtonPlaceable.place(
                        x = 1 + depthLinesPlaceable.width - depthLinesExpandButtonPlaceable.width / 2,
                        y = postContentPlaceable.height - depthLinesExpandButtonPlaceable.height
                    )
                }
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
private fun Post(
    post: MainPostCardUiState,
    showViewMoreReplies: Boolean,
    postCardInteractions: PostCardInteractions,
) {
    Row {
        Avatar(
            post = post,
            postCardInteractions = postCardInteractions,
        )
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Column {
            MetaData(
                post = post,
                postCardInteractions = postCardInteractions,
            )

            PostContent(
                uiState = post.postContentUiState,
                postCardInteractions = postCardInteractions,
            )

            Box(
                modifier = Modifier.height(36.dp)
            ) {
                BottomRow(
                    post = post,
                    postCardInteractions = postCardInteractions,
                )
            }

            if (showViewMoreReplies) {
                MediumTextLabel(
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    text = stringResource(id = R.string.view_more_replies)
                )
            }
        }
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun PostCardPreview() {
    PreviewTheme {
        PostCard(
            post = PostCardUiState(
                statusId = "",
                topRowMetaDataUiState = TopRowMetaDataUiState(
                    TopRowIconType.REPLY,
                    StringFactory.literal("in reply to Other person"),
                ),
                mainPostCardUiState = postCardUiStatePreview,
                depthLinesUiState = null,
            ),
            postCardInteractions = PostCardInteractionsNoOp,
        )
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun PostCardWithContentWarningPreview() {
    PreviewTheme {
        PostCard(
            post = PostCardUiState(
                statusId = "",
                topRowMetaDataUiState = TopRowMetaDataUiState(
                    TopRowIconType.REPLY,
                    StringFactory.literal("in reply to Other person"),
                ),
                mainPostCardUiState = postCardUiStatePreview.copy(
                    postContentUiState = postCardUiStatePreview.postContentUiState.copy(
                        contentWarning = "Micky mouse spoilers!"
                    )
                ),
                depthLinesUiState = null,
            ),
            postCardInteractions = PostCardInteractionsNoOp,
        )
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun PostCardPreviewWithDepth() {
    PreviewTheme {
        PostCard(
            post = PostCardUiState(
                statusId = "",
                topRowMetaDataUiState = null,
                mainPostCardUiState = postCardUiStatePreview,
                depthLinesUiState = DepthLinesUiState(
                    postDepth = 2,
                    depthLines = listOf(
                        0,
                        1,
                        2,
                    ),
                    showViewMoreRepliesText = true,
                    expandRepliesButtonUiState = ExpandRepliesButtonUiState.HIDDEN,
                ),
            ),
            postCardInteractions = PostCardInteractionsNoOp,
        )
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun PostCardPreviewWithHighDepth() {
    PreviewTheme {
        PostCard(
            post = PostCardUiState(
                statusId = "",
                topRowMetaDataUiState = null,
                mainPostCardUiState = postCardUiStatePreview,
                depthLinesUiState = DepthLinesUiState(
                    postDepth = 15,
                    depthLines = listOf(
                        0,
                        1,
                        2,
                    ),
                    expandRepliesButtonUiState = ExpandRepliesButtonUiState.HIDDEN,
                ),
            ),
            postCardInteractions = PostCardInteractionsNoOp,
        )
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun PostCardPreviewWithMinusButton() {
    PreviewTheme {
        PostCard(
            post = PostCardUiState(
                statusId = "",
                topRowMetaDataUiState = null,
                mainPostCardUiState = postCardUiStatePreview,
                depthLinesUiState = DepthLinesUiState(
                    postDepth = 2,
                    depthLines = listOf(
                        0,
                        1,
                        2,
                    ),
                    expandRepliesButtonUiState = ExpandRepliesButtonUiState.MINUS,
                ),
            ),
            postCardInteractions = PostCardInteractionsNoOp,
        )
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun PostCardPreviewWithPlusButton() {
    PreviewTheme {
        PostCard(
            post = PostCardUiState(
                statusId = "",
                topRowMetaDataUiState = null,
                mainPostCardUiState = postCardUiStatePreview,
                depthLinesUiState = DepthLinesUiState(
                    postDepth = 2,
                    depthLines = listOf(
                        0,
                        1,
                        2,
                    ),
                    expandRepliesButtonUiState = ExpandRepliesButtonUiState.PLUS,
                ),
            ),
            postCardInteractions = PostCardInteractionsNoOp,
        )
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun PostCardPreviewFirstItem() {
    PreviewTheme {
        PostCard(
            post = PostCardUiState(
                statusId = "",
                topRowMetaDataUiState = null,
                mainPostCardUiState = postCardUiStatePreview,
                depthLinesUiState = DepthLinesUiState(
                    postDepth = 1,
                    depthLines = listOf(
                        1,
                    ),
                    expandRepliesButtonUiState = ExpandRepliesButtonUiState.PLUS,
                ),
            ),
            postCardInteractions = PostCardInteractionsNoOp,
        )
    }
}

package social.firefly.core.ui.postcard.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfRadius
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.media.MediaDisplay
import social.firefly.core.ui.htmlcontent.HtmlContent
import social.firefly.core.ui.poll.Poll
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.PostContentUiState
import social.firefly.core.ui.postcard.R

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
        Column(
            verticalArrangement = Arrangement.spacedBy(
                space = 8.dp
            )
        ) {
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

            MediaDisplay(
                attachments = uiState.mediaAttachments,
                onAttachmentClicked = {
                    postCardInteractions.onMediaClicked(
                        uiState.statusId,
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
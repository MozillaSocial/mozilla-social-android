package social.firefly.post.bottombar

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import social.firefly.common.utils.FileType
import social.firefly.common.utils.getFileType
import social.firefly.common.utils.toFile
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.model.StatusVisibility
import social.firefly.core.ui.common.divider.FfDivider
import social.firefly.core.ui.common.dropdown.VisibilityDropDownButton
import social.firefly.feature.post.R
import social.firefly.post.NewPostViewModel
import social.firefly.post.poll.PollInteractions
import social.firefly.post.status.ContentWarningInteractions
import java.io.File

@Composable
internal fun BottomBar(
    bottomBarState: BottomBarState,
    pollInteractions: PollInteractions,
    contentWarningInteractions: ContentWarningInteractions,
    onMediaInserted: (Uri, File, FileType) -> Unit,
    onUploadImageClicked: () -> Unit,
    onUploadMediaClicked: () -> Unit,
    visibility: StatusVisibility,
    onVisibilitySelected: (StatusVisibility) -> Unit,
) {
    val context = LocalContext.current

    val multipleMediaLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(
                maxItems = bottomBarState.maxImages.coerceAtLeast(2),
            ),
        ) { uris ->
            uris.forEach {
                onMediaInserted(it, it.toFile(context), it.getFileType(context))
            }
        }
    val singleMediaLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.PickVisualMedia(),
        ) { uri ->
            uri?.let { onMediaInserted(it, it.toFile(context), it.getFileType(context)) }
        }

    BottomBar(
        bottomBarState = bottomBarState,
        onUploadImageClicked = {
            onUploadImageClicked()
            when (bottomBarState.maxImages) {
                1 -> {
                    singleMediaLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                    )
                }

                in 2..NewPostViewModel.MAX_IMAGES -> {
                    multipleMediaLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                    )
                }

                else -> {}
            }
        },
        onUploadVideoClicked = {
            onUploadMediaClicked()
            singleMediaLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly),
            )
        },
        pollInteractions = pollInteractions,
        contentWarningInteractions = contentWarningInteractions,
        visibility = visibility,
        onVisibilitySelected = onVisibilitySelected,
    )
}

@Composable
internal fun BottomBar(
    bottomBarState: BottomBarState,
    onUploadImageClicked: () -> Unit,
    onUploadVideoClicked: () -> Unit,
    pollInteractions: PollInteractions,
    contentWarningInteractions: ContentWarningInteractions,
    visibility: StatusVisibility,
    onVisibilitySelected: (StatusVisibility) -> Unit,
) {
    BottomBar(
        onUploadImageClicked = onUploadImageClicked,
        onUploadVideoClicked = onUploadVideoClicked,
        imageButtonEnabled = bottomBarState.imageButtonEnabled,
        videoButtonEnabled = bottomBarState.videoButtonEnabled,
        pollButtonEnabled = bottomBarState.pollButtonEnabled,
        contentWarningText = bottomBarState.contentWarningText,
        characterCountText = bottomBarState.characterCountText,
        pollInteractions = pollInteractions,
        contentWarningInteractions = contentWarningInteractions,
        visibility = visibility,
        onVisibilitySelected = onVisibilitySelected,
    )
}

@Composable
internal fun BottomBar(
    onUploadImageClicked: () -> Unit,
    onUploadVideoClicked: () -> Unit,
    imageButtonEnabled: Boolean,
    contentWarningText: String?,
    pollInteractions: PollInteractions,
    pollButtonEnabled: Boolean,
    contentWarningInteractions: ContentWarningInteractions,
    characterCountText: String,
    videoButtonEnabled: Boolean,
    visibility: StatusVisibility,
    onVisibilitySelected: (StatusVisibility) -> Unit,
) {
    Column {
        FfDivider(
            color = FfTheme.colors.borderPrimary,
        )
        Row(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .background(FfTheme.colors.layer1)
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onUploadImageClicked,
                enabled = imageButtonEnabled,
            ) {
                Icon(
                    FfIcons.image(),
                    contentDescription = stringResource(id = R.string.add_photos_content_description),
                )
            }

            IconButton(
                onClick = onUploadVideoClicked,
                enabled = videoButtonEnabled,
            ) {
                Icon(
                    FfIcons.monitorPlay(),
                    contentDescription = stringResource(id = R.string.add_video_content_description),
                )
            }

            AddPollButton(
                pollInteractions = pollInteractions,
                pollButtonEnabled = pollButtonEnabled,
            )

            ContentWarningButton(
                contentWarningInteractions = contentWarningInteractions,
                contentWarningText = contentWarningText,
            )

            Spacer(modifier = Modifier.weight(1f))

            VisibilityDropDownButton(
                visibility = visibility,
                onVisibilitySelected = onVisibilitySelected,
            )

            CharacterCountLabel(characterCountText = characterCountText)
        }
    }
}

@Composable
private fun AddPollButton(
    pollInteractions: PollInteractions,
    pollButtonEnabled: Boolean,
) {
    IconButton(
        onClick = {
            pollInteractions.onNewPollClicked()
        },
        enabled = pollButtonEnabled,
    ) {
        Icon(
            FfIcons.chartBar(),
            stringResource(id = R.string.add_poll_button_content_description),
        )
    }
}

@Composable
private fun ContentWarningButton(
    contentWarningInteractions: ContentWarningInteractions,
    contentWarningText: String?,
) {
    IconButton(
        onClick = { contentWarningInteractions.onContentWarningClicked() },
    ) {
        Icon(
            FfIcons.warning(),
            stringResource(id = R.string.content_warning_button_content_description),
            tint =
            if (contentWarningText == null) {
                LocalContentColor.current
            } else {
                FfTheme.colors.textWarning
            },
        )
    }
}

@Composable
private fun CharacterCountLabel(characterCountText: String) {
    Text(
        modifier =
        Modifier
            .wrapContentSize()
            .padding(FfSpacing.md),
        text = characterCountText,
        style = FfTheme.typography.labelSmall,
        color = FfTheme.colors.textSecondary,
    )
}

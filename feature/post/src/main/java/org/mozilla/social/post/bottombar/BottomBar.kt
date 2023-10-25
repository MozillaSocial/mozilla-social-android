package org.mozilla.social.post.bottombar

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import org.mozilla.social.common.utils.FileType
import org.mozilla.social.common.utils.getFileType
import org.mozilla.social.common.utils.toFile
import org.mozilla.social.core.designsystem.component.MoSoDivider
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoSpacing
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.feature.post.R
import org.mozilla.social.post.NewPostViewModel
import org.mozilla.social.post.poll.PollInteractions
import org.mozilla.social.post.status.ContentWarningInteractions
import java.io.File

@Composable
internal fun BottomBar(
    bottomBarState: BottomBarState,
    pollInteractions: PollInteractions,
    contentWarningInteractions: ContentWarningInteractions,
    onMediaInserted: (Uri, File, FileType) -> Unit,
) {
    val context = LocalContext.current
    val multipleMediaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(
            maxItems = NewPostViewModel.MAX_IMAGES,
        )
    ) { uris ->
        uris.forEach {
            onMediaInserted(it, it.toFile(context), it.getFileType(context))
        }
    }
    val singleMediaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { onMediaInserted(it, it.toFile(context), it.getFileType(context)) }
    }

    BottomBar(
        bottomBarState = bottomBarState,
        onUploadImageClicked = {
            multipleMediaLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onUploadVideoClicked = {
            singleMediaLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
            )
        },
        pollInteractions = pollInteractions,
        contentWarningInteractions = contentWarningInteractions,
    )

}

@Composable
internal fun BottomBar(
    bottomBarState: BottomBarState,
    onUploadImageClicked: () -> Unit,
    onUploadVideoClicked: () -> Unit,
    pollInteractions: PollInteractions,
    contentWarningInteractions: ContentWarningInteractions,
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
) {
    Column {
        MoSoDivider(
            color = MoSoTheme.colors.borderPrimary
        )
        Row(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
                .background(MoSoTheme.colors.layer1),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onUploadImageClicked,
                enabled = imageButtonEnabled,
            ) {
                Icon(
                    MoSoIcons.image(),
                    contentDescription = stringResource(id = R.string.add_photos_content_description),
                )
            }

            Spacer(modifier = Modifier.width(MoSoSpacing.sm))

            IconButton(
                onClick = onUploadVideoClicked,
                enabled = videoButtonEnabled,
            ) {
                Icon(
                    MoSoIcons.monitorPlay(),
                    contentDescription = stringResource(id = R.string.add_video_content_description),
                )
            }

            Spacer(modifier = Modifier.width(MoSoSpacing.sm))
            AddPollButton(
                pollInteractions = pollInteractions,
                pollButtonEnabled = pollButtonEnabled
            )
            Spacer(modifier = Modifier.width(MoSoSpacing.sm))
            ContentWarningButton(
                contentWarningInteractions = contentWarningInteractions,
                contentWarningText = contentWarningText,
            )
            Spacer(modifier = Modifier.weight(1f))
            CharacterCountLabel(characterCountText = characterCountText)
        }
    }
}

@Composable
private fun AddPollButton(pollInteractions: PollInteractions, pollButtonEnabled: Boolean) {
    IconButton(
        onClick = { pollInteractions.onNewPollClicked() },
        enabled = pollButtonEnabled,
    ) {
        Icon(
            MoSoIcons.chartBar(),
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
            MoSoIcons.warning(),
            stringResource(id = R.string.content_warning_button_content_description),
            tint = if (contentWarningText == null) {
                LocalContentColor.current
            } else {
                MoSoTheme.colors.textWarning
            },
        )
    }
}

@Composable
private fun CharacterCountLabel(characterCountText: String) {
    Text(
        modifier = Modifier
            .wrapContentSize()
            .padding(MoSoSpacing.md),
        text = characterCountText,
        style = MoSoTheme.typography.labelSmall,
        color = MoSoTheme.colors.textSecondary,
    )
}

data class BottomBarState(
    val imageButtonEnabled: Boolean = false,
    val videoButtonEnabled: Boolean = false,
    val pollButtonEnabled: Boolean = false,
    val contentWarningText: String? = null,
    val characterCountText: String = "",
)
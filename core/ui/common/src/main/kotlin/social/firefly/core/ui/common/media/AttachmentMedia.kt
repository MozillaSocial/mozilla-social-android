package social.firefly.core.ui.common.media

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import social.firefly.common.LoadState
import social.firefly.common.utils.FileType
import social.firefly.common.utils.toFile
import social.firefly.core.designsystem.theme.ColorPalette
import social.firefly.core.ui.common.R
import social.firefly.core.ui.common.TransparentOverlay
import java.io.File

@Composable
fun AttachmentMedia(
    uri: Uri,
    loadState: LoadState,
    fileType: FileType,
    onRetryClicked: (Uri, File, FileType) -> Unit,
) {
    Box {
        val context = LocalContext.current

        when (fileType) {
            FileType.VIDEO -> {
                VideoPlayer(uri = uri, loadState = loadState)
            }

            FileType.IMAGE -> Image(imageUri = uri)
            FileType.UNKNOWN -> {}
        }

        when (loadState) {
            LoadState.LOADING -> {
                TransparentOverlay()
                Column(
                    modifier =
                    Modifier
                        .align(Alignment.Center),
                ) {
                    Text(
                        text = stringResource(id = R.string.media_uploading),
                        color = ColorPalette.White,
                    )
                    CircularProgressIndicator(
                        modifier =
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                    )
                }
            }

            LoadState.ERROR -> {
                TransparentOverlay()
                Column(
                    modifier = Modifier.align(Alignment.Center),
                ) {
                    Text(
                        text = stringResource(id = R.string.media_upload_failed),
                        color = ColorPalette.White,
                    )
                    Button(
                        modifier =
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                        onClick = {
                            onRetryClicked(uri, uri.toFile(context), fileType)
                        },
                    ) {
                        Text(
                            text = stringResource(id = R.string.retry),
                            color = ColorPalette.White,
                        )
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
private fun Image(imageUri: Uri) {
    AsyncImage(
        modifier =
        Modifier
            .fillMaxWidth(),
        model = imageUri,
        contentDescription = "",
        contentScale = ContentScale.FillWidth,
    )
}

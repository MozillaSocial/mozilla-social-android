package org.mozilla.social.core.ui.images

import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.mozilla.social.common.LoadState
import org.mozilla.social.common.utils.toFile
import org.mozilla.social.core.designsystem.theme.FirefoxColor
import java.io.File

@Composable
fun ImageToUpload(
    imageUri: Uri,
    loadState: LoadState,
    onRetryClicked: (Uri, File) -> Unit,
) {
    Box(
        modifier = Modifier
            .background(FirefoxColor.Black)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(
                    if (loadState == LoadState.LOADING) {
                        0.3f
                    } else {
                        1f
                    }
                ),
            model = imageUri,
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
        )
        when (loadState) {
            LoadState.LOADING -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            LoadState.ERROR -> {
                val context = LocalContext.current
                Column(
                    modifier = Modifier.align(Alignment.Center),
                ) {
                    Text(
                        text = "Failed to upload image",
                        color = FirefoxColor.White,
                    )
                    Button(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                        onClick = {
                            onRetryClicked(imageUri, imageUri.toFile(context))
                        }
                    ) {
                        Text(
                            text = "Retry",
                            color = FirefoxColor.White,
                        )
                    }
                }
            }
            LoadState.LOADED -> {}
        }
    }
}
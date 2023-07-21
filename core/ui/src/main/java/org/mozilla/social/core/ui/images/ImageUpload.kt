package org.mozilla.social.core.ui.images

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.mozilla.social.common.utils.toFile
import org.mozilla.social.core.designsystem.theme.FirefoxColor
import java.io.File

@Composable
fun rememberImageBitmap(
    imageUri: Uri,
) : ImageBitmap {
    val context = LocalContext.current
    return remember {
        val bitmap: Bitmap = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        }
        bitmap.asImageBitmap()
    }

}

@Composable
fun UploadImageLoading(
    bitmap: ImageBitmap,
) {
    Box(
        modifier = Modifier
            .background(FirefoxColor.Black)
    ) {
        Image(
            modifier = Modifier.alpha(.3f),
            bitmap = bitmap,
            contentDescription = null,
        )
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun UploadImageError(
    bitmap: ImageBitmap,
    imageUri: Uri,
    onRetryClicked: (File) -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .background(FirefoxColor.Black)
    ) {
        Image(
            modifier = Modifier.alpha(.3f),
            bitmap = bitmap,
            contentDescription = null,
        )
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
                    onRetryClicked(imageUri.toFile(context))
                }
            ) {
                Text(
                    text = "Retry",
                    color = FirefoxColor.White,
                )
            }
        }
    }
}
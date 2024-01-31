package org.mozilla.social.core.ui.zoom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.mozilla.social.core.ui.common.utils.PreviewTheme

@Composable
fun ImageZoom(
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color(0x55FFFFFF)
                )
                .blur(16.dp)
        ) {
            Text(text = "test")
        }
    }
}

@Preview
@Composable
private fun ImageZoomPreview() {
    PreviewTheme {
        ImageZoom(
            onDismissRequest = {}
        )
    }
}

package social.firefly.core.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import social.firefly.core.designsystem.utils.NoRipple

@Composable
fun TransparentOverlay(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Color(OVERLAY_COLOR)),
    )
}

private const val OVERLAY_COLOR = 0xAA000000

@Composable
fun TransparentNoTouchOverlay(modifier: Modifier = Modifier) {
    NoRipple {
        Box(
            modifier =
                modifier
                    .fillMaxSize()
                    .background(Color(OVERLAY_COLOR))
                    .clickable { },
        )
    }
}

@Composable
fun NoTouchOverlay(modifier: Modifier = Modifier) {
    NoRipple {
        Box(
            modifier =
                modifier
                    .fillMaxSize()
                    .clickable { },
        )
    }
}

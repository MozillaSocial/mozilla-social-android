package org.mozilla.social.core.ui.zoom

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import org.mozilla.social.core.designsystem.theme.FirefoxColor
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.utils.PreviewTheme

@Composable
fun ImageZoom(
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = true
        )
    ) {
        val activityWindow = getActivityWindow()
        val dialogWindow = getDialogWindow()
        val parentView = LocalView.current.parent as View
        SideEffect {
            if (activityWindow != null && dialogWindow != null) {
                val attributes = WindowManager.LayoutParams()
                attributes.copyFrom(activityWindow.attributes)
                attributes.type = dialogWindow.attributes.type
                dialogWindow.attributes = attributes
                parentView.layoutParams = FrameLayout.LayoutParams(
                    activityWindow.decorView.width,
                    activityWindow.decorView.height
                )
            }
        }

//        val systemUiController = rememberSystemUiController(getActivityWindow())
//        val dialogSystemUiController = rememberSystemUiController(getDialogWindow())
//
//        DisposableEffect(Unit) {
//            systemUiController.setSystemBarsColor(color = Color.Transparent)
//            dialogSystemUiController.setSystemBarsColor(color = Color.Transparent)
//
//            onDispose {
//                systemUiController.setSystemBarsColor(color = previousSystemBarsColor)
//                dialogSystemUiController.setSystemBarsColor(color = previousSystemBarsColor)
//            }
//        }

//        MoSoSurface(
//            modifier = Modifier
//                .fillMaxSize()
//        ) {
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

//        }
    }
}

@Composable
fun getDialogWindow(): Window? = (LocalView.current.parent as? DialogWindowProvider)?.window

@Composable
fun getActivityWindow(): Window? = LocalView.current.context.getActivityWindow()

private tailrec fun Context.getActivityWindow(): Window? =
    when (this) {
        is Activity -> window
        is ContextWrapper -> baseContext.getActivityWindow()
        else -> null
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

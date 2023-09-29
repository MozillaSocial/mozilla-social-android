package org.mozilla.social.core.designsystem.component

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.SharedFlow
import org.mozilla.social.common.utils.StringFactory

@Composable
fun MoSoToast(
    toastMessage: SharedFlow<StringFactory>,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        toastMessage.collect {
            Toast.makeText(context, it.build(context), Toast.LENGTH_LONG).show()
        }
    }
}
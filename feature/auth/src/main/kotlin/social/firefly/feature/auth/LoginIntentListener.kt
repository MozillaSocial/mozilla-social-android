package social.firefly.feature.auth

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.util.Consumer
import social.firefly.core.usecase.mastodon.auth.Login

@Composable
fun LoginIntentListener(
    onUserCodeReceived: (String) -> Unit,
) {
    val activity = LocalContext.current as? ComponentActivity

    DisposableEffect(Unit) {
        val listener = Consumer<Intent> { intent ->
            intent.data?.let { data ->
                if (data.toString().startsWith(Login.AUTH_SCHEME)) {
                    data.getQueryParameter(Login.CODE)?.let { userCode ->
                        onUserCodeReceived(userCode)
                    }
                }
            }
        }
        activity?.addOnNewIntentListener(listener)
        onDispose { activity?.removeOnNewIntentListener(listener) }
    }
}
package org.mozilla.social

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.ui.MainActivityScreen

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MoSoTheme {
                MoSoSurface(modifier = Modifier.fillMaxSize()) {
                    MainActivityScreen(
                        context = this,
                        navigationEvents = viewModel.navigationEvents,
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        viewModel.onNewIntentReceived(intent)
    }
}


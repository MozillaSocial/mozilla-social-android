package org.mozilla.social

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.ui.MainActivityScreen

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MoSoTheme {
                MoSoSurface(modifier = Modifier.fillMaxSize()) {
                    KoinAndroidContext {
                        viewModel = getViewModel()
                        MainActivityScreen()
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        viewModel.onNewIntentReceived(intent)
    }
}


package org.mozilla.social

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.workmanager.setupPurgeWork
import org.mozilla.social.ui.MainActivityScreen
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.preloadData()
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MoSoTheme {
                MoSoSurface(modifier = Modifier.fillMaxSize()) {
                    KoinAndroidContext {
                        MainActivityScreen()
                    }
                }
            }
        }

        setupPurgeWork(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        try {
            viewModel.onNewIntentReceived(intent)
        } catch (exception: Exception) {
            Timber.e("caught exception: $exception")
        }
    }
}

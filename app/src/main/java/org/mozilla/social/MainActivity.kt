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
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.mozilla.social.core.analytics.AppAnalytics
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.workmanager.DatabasePurgeWorker
import org.mozilla.social.ui.MainActivityScreen
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel()
    private val analytics: AppAnalytics by inject()

    @OptIn(KoinExperimentalAPI::class)
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

        DatabasePurgeWorker.setupPurgeWork(this, lifecycleScope)
    }

    override fun onResume() {
        super.onResume()
        analytics.appOpened()
    }

    override fun onPause() {
        super.onPause()
        analytics.appBackgrounded()
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

package social.firefly

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.annotation.KoinExperimentalAPI
import social.firefly.core.analytics.AppAnalytics
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.designsystem.theme.ThemeOption
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.workmanager.DatabasePurgeWorker
import social.firefly.ui.MainActivityScreen

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel()
    private val analytics: AppAnalytics by inject()

    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val themeOption by viewModel.themeOption.collectAsStateWithLifecycle(
                initialValue = ThemeOption.SYSTEM
            )

            FfTheme(
                themeOption = themeOption,
            ) {
                FfSurface(modifier = Modifier.fillMaxSize()) {
                    KoinAndroidContext {
                        MainActivityScreen()
                    }
                }
            }

            // initialize the view model in the setContent block so that initialize is called
            // again when the layout inspector starts
            LaunchedEffect(Unit) {
                viewModel.initialize(intent)
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
        viewModel.handleIntent(intent)
    }
}

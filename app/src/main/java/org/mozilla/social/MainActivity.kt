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
import org.mozilla.social.core.workmanager.DatabasePurgeWorker
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

        DatabasePurgeWorker.setupPurgeWork(this, lifecycleScope)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        try {
            viewModel.onNewIntentReceived(intent)
        } catch (exception: Exception) {
            Timber.e("caught exception: $exception")
        }
    }

    private fun setupAboutLibraries() {
//        val libs = Libs.Builder()
//            .withJson(aboutLibsJson) // provide the metaData (alternative APIs available)
//            .build()
//        val libraries = libs.libraries // retrieve all libraries defined in the metadata
//        val licenses = libs.licenses // retrieve all licenses defined in the metadata
//        for (lib in libraries) {
//            Log.i("AboutLibraries", "${lib.name}")
//        }
    }
}

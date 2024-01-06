package org.mozilla.social.feature.settings.developer

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar
import org.mozilla.social.core.ui.common.button.MoSoButton
import org.mozilla.social.core.workmanager.testPurge

@Composable
fun DeveloperOptionsScreen() {
    MoSoSurface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
    ) {
        Column {
            MoSoCloseableTopAppBar()
            val context = LocalContext.current
            MoSoButton(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    testPurge(
                        context as Activity,
                        (context as LifecycleOwner).lifecycleScope
                    )
                }
            ) {
                Text(text = "Test database purge")
            }
        }
    }
}
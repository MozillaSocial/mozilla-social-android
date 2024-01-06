package org.mozilla.social.feature.settings.developer

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar
import org.mozilla.social.core.ui.common.button.MoSoButton
import org.mozilla.social.core.ui.common.text.MoSoTextField
import org.mozilla.social.core.workmanager.testPurge
import java.time.Duration

@Composable
fun DeveloperOptionsScreen() {
    MoSoSurface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
    ) {
        Column {
            MoSoCloseableTopAppBar()

            Row(
                modifier = Modifier
                    .padding(8.dp),
            ) {
                val context = LocalContext.current
                var delay by remember {
                    mutableStateOf("5")
                }

                MoSoTextField(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f),
                    value = delay,
                    onValueChange = {
                        delay = it
                    },
                    label = {
                        Text(text = "Delay in seconds")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                )

                MoSoButton(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(8.dp)
                        .weight(1f),
                    onClick = {
                        testPurge(
                            context as Activity,
                            (context as LifecycleOwner).lifecycleScope,
                            Duration.ofSeconds(delay.toLongOrNull() ?: 0)
                        )
                    }
                ) {
                    Text(text = "Test database purge")
                }
            }

        }
    }
}
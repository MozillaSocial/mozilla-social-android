package org.mozilla.social.feature.settings.developer

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.ui.common.MoSoCheckBox
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar
import org.mozilla.social.core.ui.common.button.MoSoButton
import org.mozilla.social.core.ui.common.text.MediumTextLabel
import org.mozilla.social.core.ui.common.text.MoSoTextField
import org.mozilla.social.core.ui.common.utils.PreviewTheme
import org.mozilla.social.core.workmanager.DatabasePurgeWorker
import org.mozilla.social.feature.settings.R
import java.time.Duration

@Composable
fun DeveloperOptionsScreen() {
    MoSoSurface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
    ) {
        Column {
            MoSoCloseableTopAppBar(
                title = stringResource(id = R.string.developer_options_title)
            )

            val context = LocalContext.current
            var delay by remember {
                mutableStateOf("0")
            }
            var restartActivityChecked by remember {
                mutableStateOf(true)
            }

            Row(
                modifier = Modifier
                    .padding(8.dp),
            ) {
                MoSoTextField(
                    modifier = Modifier
                        .width(150.dp)
                        .align(Alignment.CenterVertically),
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

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable { restartActivityChecked = !restartActivityChecked },
                ) {
                    MoSoCheckBox(
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        checked = restartActivityChecked,
                        onCheckedChange = { restartActivityChecked = it }
                    )

                    MediumTextLabel(
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        text = "Restart activity",
                    )
                }

            }

            MoSoButton(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                onClick = {
                    DatabasePurgeWorker.setupTestPurgeWork(
                        context as Activity,
                        (context as LifecycleOwner).lifecycleScope,
                        Duration.ofSeconds(delay.toLongOrNull() ?: 0),
                        restartActivity = restartActivityChecked,
                    )
                }
            ) {
                MediumTextLabel(text = "Test database purge")
            }
        }
    }
}

@Preview
@Composable
private fun DeveloperOptionsScreenPreview() {
    PreviewTheme(
        modules = listOf(navigationModule)
    ) {
        DeveloperOptionsScreen()
    }
}

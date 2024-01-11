package org.mozilla.social.feature.settings.licenses

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.theme.MoSoSpacing
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.text.MediumTextBody
import org.mozilla.social.feature.settings.R
import org.mozilla.social.feature.settings.ui.SettingsColumn
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer

@Composable
fun OpenSourceLicensesScreen(viewModel: OpenSourceLicensesViewModel = koinViewModel()) {
    OpenSourceLicensesScreen()

    LaunchedEffect(Unit) {
    }
}

@Composable
private fun OpenSourceLicensesScreen() {
    val context = LocalContext.current

    MoSoSurface {
        SettingsColumn(title = stringResource(id = R.string.settings_title)) {
            LibrariesContainer(
                Modifier.fillMaxSize()
            )
        }
    }
}
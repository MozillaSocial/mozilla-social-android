package org.mozilla.social.feature.settings.licenses

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.feature.settings.R
import org.mozilla.social.feature.settings.ui.SettingsColumn

@Composable
fun OpenSourceLicensesScreen() {
    OpenSourceLicensesScreenLibrary()

    LaunchedEffect(Unit) {
    }
}

@Composable
private fun OpenSourceLicensesScreenLibrary() {

    val customDefaults = LibraryDefaults.libraryPadding(
        versionPadding = PaddingValues(12.dp),
        badgeContentPadding = PaddingValues(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
        badgePadding = PaddingValues(4.dp)
    )

    MoSoSurface {
        SettingsColumn(title = stringResource(id = R.string.settings_title)) {
            LibrariesContainer(
                modifier = Modifier
                    .fillMaxSize(),
                padding = customDefaults
            )
        }
    }
}
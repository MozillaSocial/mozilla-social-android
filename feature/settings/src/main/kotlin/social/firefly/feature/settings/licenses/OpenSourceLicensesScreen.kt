package social.firefly.feature.settings.licenses

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.FfSurface
import social.firefly.feature.settings.R
import social.firefly.feature.settings.ui.SettingsColumn

@Composable
fun OpenSourceLicensesScreen(
    viewModel: OpenSourceLicensesViewModel = koinViewModel()
) {
    OpenSourceLicensesScreenLibrary()

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@Composable
private fun OpenSourceLicensesScreenLibrary() {

    val customPadding = FfLibraryDefaults.FfLibraryPadding(
        versionPadding = PaddingValues(12.dp),
        badgeContentPadding = PaddingValues(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
        badgePadding = PaddingValues(4.dp),
    )

    val customColors = FfLibraryDefaults.FfLibraryColors(
        backgroundColor = FfTheme.colors.layer1,
        contentColor = FfTheme.colors.textPrimary,
        badgeBackgroundColor = FfTheme.colors.layer2,
        badgeContentColor = FfTheme.colors.textSecondary,
        dialogConfirmButtonColor = FfTheme.colors.iconActionActive
    )

    FfSurface {
        SettingsColumn(title = stringResource(id = R.string.licenses_settings_title)) {
            FfLibrariesContainer(
                modifier = Modifier
                    .fillMaxSize(),
                padding = customPadding,
                colors = customColors,
            )
        }
    }
}
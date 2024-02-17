package social.firefly.feature.settings.licenses

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.designsystem.theme.MoSoTheme
import social.firefly.core.ui.common.MoSoSurface
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

    val customPadding = MoSoLibraryDefaults.moSoLibraryPadding(
        versionPadding = PaddingValues(12.dp),
        badgeContentPadding = PaddingValues(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
        badgePadding = PaddingValues(4.dp),
    )

    val customColors = MoSoLibraryDefaults.moSoLibraryColors(
        backgroundColor = MoSoTheme.colors.layer1,
        contentColor = MoSoTheme.colors.textPrimary,
        badgeBackgroundColor = MoSoTheme.colors.layer2,
        badgeContentColor = MoSoTheme.colors.textSecondary,
        dialogConfirmButtonColor = MoSoTheme.colors.iconActionActive
    )

    MoSoSurface {
        SettingsColumn(title = stringResource(id = R.string.licenses_settings_title)) {
            MoSoLibrariesContainer(
                modifier = Modifier
                    .fillMaxSize(),
                padding = customPadding,
                colors = customColors,
            )
        }
    }
}
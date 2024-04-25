package social.firefly.feature.settings.appearance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.appbar.FfCloseableTopAppBar
import social.firefly.feature.settings.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppearanceAndBehaviorScreen(
    viewModel: AppearanceAndBehaviorViewModel = koinViewModel()
) {
    FfSurface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
    ) {
        Column {
            FfCloseableTopAppBar(
                title = stringResource(id = R.string.appearance_and_behavior_title)
            )
        }
    }
}
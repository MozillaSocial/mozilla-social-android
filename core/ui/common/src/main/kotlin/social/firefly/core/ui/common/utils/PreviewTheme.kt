package social.firefly.core.ui.common.utils

import androidx.compose.runtime.Composable
import org.koin.compose.KoinApplication
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.designsystem.theme.ThemeOption
import social.firefly.core.ui.common.FfSurface

@Composable
fun PreviewTheme(
    themeOption: ThemeOption = ThemeOption.DARK,
    modules: List<Module> = emptyList(),
    content: @Composable () -> Unit,
) {
    KoinApplication(application = {
        modules(modules)
    }) {
        FfTheme(
            themeOption = themeOption,
        ) {
            FfSurface {
                content()
            }
        }
        stopKoin()
    }
}

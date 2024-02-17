package social.firefly.core.ui.common.utils

import androidx.compose.runtime.Composable
import org.koin.compose.KoinApplication
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import social.firefly.core.designsystem.theme.MoSoTheme
import social.firefly.core.ui.common.MoSoSurface

@Composable
fun PreviewTheme(
    darkTheme: Boolean = false,
    modules: List<Module> = emptyList(),
    content: @Composable () -> Unit,
) {
    KoinApplication(application = {
        modules(modules)
    }) {
        MoSoTheme(
            darkTheme = darkTheme,
        ) {
            MoSoSurface {
                content()
            }
        }
        stopKoin()
    }
}

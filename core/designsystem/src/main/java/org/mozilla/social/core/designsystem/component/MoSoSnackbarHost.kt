package org.mozilla.social.core.designsystem.component


import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Wrapper for [SnackbarHost] for use with [MoSoSnackbar] and [MoSoSnackbarHostState], which
 * adds the [SnackbarType] parameter
 */
@Composable
fun MoSoSnackbarHost(
    hostState: MoSoSnackbarHostState,
    modifier: Modifier = Modifier,
    snackbar: @Composable (SnackbarData, SnackbarType) -> Unit = { snackbarData, snackbarType ->
        MoSoSnackbar(snackbarData = snackbarData, snackbarType = snackbarType)
    }
) {
    val currentSnackbarType = hostState.currentSnackbarType

    SnackbarHost(hostState.snackbarHostState, modifier, snackbar = { snackbarData ->
        currentSnackbarType?.let {
            snackbar(snackbarData, it)
        }
    })
}

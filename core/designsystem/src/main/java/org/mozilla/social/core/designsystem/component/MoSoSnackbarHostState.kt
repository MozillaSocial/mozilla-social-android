package org.mozilla.social.core.designsystem.component

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Wrapper for [SnackbarHostState] for use with [MoSoSnackbar] and [MoSoSnackbarHost]
 */
class MoSoSnackbarHostState(val snackbarHostState: SnackbarHostState = SnackbarHostState()) {
    var currentSnackbarType by mutableStateOf<SnackbarType?>(null)
        private set

    private val mutex = Mutex()

    /**
     * Wrapper for [SnackbarHostState.showSnackbar] which takes in the additional parameter
     * [SnackbarType] for special styling of [MoSoSnackbar]
     */
    suspend fun showSnackbar(
        snackbarType: SnackbarType,
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration =
            if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite
    ): SnackbarResult {
        mutex.withLock {
            currentSnackbarType = snackbarType
            return snackbarHostState.showSnackbar(
                message,
                actionLabel,
                withDismissAction,
                duration
            )
        }
    }
}
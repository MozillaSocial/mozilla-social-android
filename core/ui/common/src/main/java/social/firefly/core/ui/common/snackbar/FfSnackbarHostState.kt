/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package social.firefly.core.ui.common.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Wrapper for [SnackbarHostState] for use with [FfSnackbar] and [FfSnackbarHost]
 */
class FfSnackbarHostState(val snackbarHostState: SnackbarHostState = SnackbarHostState()) {
    var currentSnackbarType by mutableStateOf<SnackbarType?>(null)
        private set

    private val mutex = Mutex()

    /**
     * Wrapper for [SnackbarHostState.showSnackbar] which takes in the additional parameter
     * [SnackbarType] for special styling of [FfSnackbar]
     */
    suspend fun showSnackbar(
        snackbarType: SnackbarType,
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration =
            if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
    ): SnackbarResult {
        mutex.withLock {
            currentSnackbarType = snackbarType
            return snackbarHostState.showSnackbar(
                message,
                actionLabel,
                withDismissAction,
                duration,
            )
        }
    }
}

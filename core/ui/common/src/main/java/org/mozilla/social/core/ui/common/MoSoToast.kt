package org.mozilla.social.core.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.SharedFlow
import org.koin.compose.koinInject
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.navigation.usecases.ShowSnackbar

@Composable
fun MoSoErrorToast(
    toastMessage: SharedFlow<StringFactory>,
    showSnackbar: ShowSnackbar = koinInject(),
) {
    LaunchedEffect(Unit) {
        toastMessage.collect {
            showSnackbar(it, isError = true)
        }
    }
}
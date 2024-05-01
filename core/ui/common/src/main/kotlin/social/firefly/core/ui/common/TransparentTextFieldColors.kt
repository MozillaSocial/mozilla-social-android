package social.firefly.core.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import social.firefly.core.ui.common.text.FfTextFieldDefaults

@Composable
fun transparentTextFieldColors() =
    FfTextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
    )

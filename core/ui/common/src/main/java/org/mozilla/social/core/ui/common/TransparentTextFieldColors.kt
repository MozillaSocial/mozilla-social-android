package org.mozilla.social.core.ui.common

import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.mozilla.social.core.ui.common.text.MoSoTextFieldDefaults

@Composable
fun transparentTextFieldColors() =
    MoSoTextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
    )

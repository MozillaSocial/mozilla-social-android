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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import social.firefly.core.designsystem.theme.FfRadius
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.button.FfButton

/**
 * Wrapper for [Snackbar] which takes in a [SnackbarType], which is used to determine the styling
 * of the resulting snackbar. Note that this implementation does not include an action button
 */
@Composable
fun FfSnackbar(snackbarState: FfSnackbarState) =
    with(snackbarState) { FfSnackbar(snackbarData = snackbarData, snackbarType = snackbarType) }

/**
 * Wrapper for [Snackbar] which takes in a [SnackbarType], which is used to determine the styling
 * of the resulting snackbar. Note that this implementation does not include an action button
 */
@Composable
fun FfSnackbar(
    snackbarData: SnackbarData,
    snackbarType: SnackbarType,
) {
    when (snackbarType) {
        SnackbarType.SUCCESS -> {
            FfSuccessSnackbar(snackbarData = snackbarData)
        }

        SnackbarType.ERROR -> {
            FfErrorSnackbar(snackbarData = snackbarData)
        }
    }
}

/**
 * A snackbar denoting success
 */
@Composable
private fun FfSuccessSnackbar(snackbarData: SnackbarData) {
    FfSnackbar(
        snackbarData = snackbarData,
        backgroundColor = FfTheme.colors.snackbarBkgSuccess,
        borderColor = FfTheme.colors.snackbarBorderSuccess,
        contentColor = FfTheme.colors.snackbarTextSuccess,
    )
}

/**
 * A snackbar denoting an error
 */
@Composable
private fun FfErrorSnackbar(snackbarData: SnackbarData) {
    FfSnackbar(
        snackbarData = snackbarData,
        backgroundColor = FfTheme.colors.snackbarBkgError,
        borderColor = FfTheme.colors.snackbarBorderError,
        contentColor = FfTheme.colors.snackbarTextError,
    )
}

/**
 * Snackbar with custom styling, including rounded corners and a border. Note that this
 * implementation does not include an action button
 */
@Composable
private fun FfSnackbar(
    snackbarData: SnackbarData,
    backgroundColor: Color,
    borderColor: Color,
    contentColor: Color,
) {
    Surface(
        modifier =
        Modifier
            .defaultMinSize(minHeight = 32.dp),
        color = backgroundColor,
        border = BorderStroke(width = 1.dp, brush = SolidColor(borderColor)),
        shape = RoundedCornerShape(FfRadius.md_8_dp),
    ) {
        Box(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(FfSpacing.sm),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = snackbarData.visuals.message,
                style = FfTheme.typography.labelSmall,
                color = contentColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/**
 * Represents whether the snackbar is communicating success or an error
 */
enum class SnackbarType {
    SUCCESS,
    ERROR,
}

data class FfSnackbarState(
    val snackbarType: SnackbarType,
    val snackbarData: SnackbarData,
)

@Preview
@Composable
private fun SnackbarPreview() {
    val snackbarHostState = FfSnackbarHostState()
    val scope = rememberCoroutineScope()
    FfTheme {
        Surface {
            Column(
                modifier =
                Modifier
                    .fillMaxSize()
                    .padding(FfSpacing.lg),
                verticalArrangement = Arrangement.Bottom,
            ) {
                FfSnackbarHost(snackbarHostState) { snackbarData, snackbarType ->
                    FfSnackbar(snackbarData = snackbarData, snackbarType = snackbarType)
                }

                FfButton(onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            snackbarType = SnackbarType.SUCCESS,
                            message = "Here's a success snackbar",
                            duration = SnackbarDuration.Short,
                        )
                    }
                }) {
                    Text(text = "show success snackbar")
                }

                FfButton(onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            snackbarType = SnackbarType.ERROR,
                            message = "Here's an error snackbar",
                            duration = SnackbarDuration.Short,
                        )
                    }
                }) {
                    Text(text = "show error snackbar")
                }

                FfButton(onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            snackbarType = SnackbarType.ERROR,
                            message =
                            "Here's an error snackbar with a really long message that " +
                                    "will show on multiple lines because it's so long",
                            duration = SnackbarDuration.Short,
                        )
                    }
                }) {
                    Text(text = "show snackbar with long text")
                }
            }
        }
    }
}

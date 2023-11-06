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

package org.mozilla.social.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.mozilla.social.core.designsystem.theme.MoSoRadius
import org.mozilla.social.core.designsystem.theme.MoSoSpacing
import org.mozilla.social.core.designsystem.theme.MoSoTheme


/**
 * Wrapper for [Snackbar] which takes in a [SnackbarType], which is used to determine the styling
 * of the resulting snackbar. Note that this implementation does not include an action button
 */
@Composable
fun MoSoSnackbar(snackbarState: MoSoSnackbarState) =
    with(snackbarState) { MoSoSnackbar(snackbarData = snackbarData, snackbarType = snackbarType) }

/**
 * Wrapper for [Snackbar] which takes in a [SnackbarType], which is used to determine the styling
 * of the resulting snackbar. Note that this implementation does not include an action button
 */
@Composable
fun MoSoSnackbar(snackbarData: SnackbarData, snackbarType: SnackbarType) {
    when (snackbarType) {
        SnackbarType.SUCCESS -> {
            MoSoSuccessSnackbar(snackbarData = snackbarData)
        }

        SnackbarType.ERROR -> {
            MoSoErrorSnackbar(snackbarData = snackbarData)
        }
    }
}

/**
 * A snackbar denoting success
 */
@Composable
private fun MoSoSuccessSnackbar(snackbarData: SnackbarData) {
    MoSoSnackbar(
        snackbarData = snackbarData,
        backgroundColor = MoSoTheme.colors.snackbarBkgSuccess,
        borderColor = MoSoTheme.colors.snackbarBorderSuccess,
        contentColor = MoSoTheme.colors.snackbarTextSuccess
    )
}

/**
 * A snackbar denoting an error
 */
@Composable
private fun MoSoErrorSnackbar(snackbarData: SnackbarData) {
    MoSoSnackbar(
        snackbarData = snackbarData,
        backgroundColor = MoSoTheme.colors.snackbarBkgError,
        borderColor = MoSoTheme.colors.snackbarBorderError,
        contentColor = MoSoTheme.colors.snackbarTextError,
    )
}

/**
 * Snackbar with custom styling, including rounded corners and a border. Note that this
 * implementation does not include an action button
 */
@Composable
private fun MoSoSnackbar(
    snackbarData: SnackbarData,
    backgroundColor: Color,
    borderColor: Color,
    contentColor: Color,
) {
    Snackbar(
        modifier = Modifier
            .border(
                border = BorderStroke(width = 2.dp, brush = SolidColor(borderColor)),
                shape = RoundedCornerShape(MoSoRadius.md)
            )
            .clip(RoundedCornerShape(MoSoRadius.md)),
        containerColor = backgroundColor,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = snackbarData.visuals.message,
                style = MoSoTheme.typography.labelSmall,
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
    SUCCESS, ERROR
}

data class MoSoSnackbarState(
    val snackbarType: SnackbarType, val snackbarData: SnackbarData
)

@Preview
@Composable
private fun SnackbarPreview() {
    val snackbarHostState = MoSoSnackbarHostState()
    val scope = rememberCoroutineScope()
    MoSoTheme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MoSoSpacing.lg),
                verticalArrangement = Arrangement.Bottom
            ) {

                MoSoSnackbarHost(snackbarHostState) { snackbarData, snackbarType ->
                    MoSoSnackbar(snackbarData = snackbarData, snackbarType = snackbarType)
                }

                MoSoButton(onClick = {
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

                MoSoButton(onClick = {
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
            }
        }
    }
}

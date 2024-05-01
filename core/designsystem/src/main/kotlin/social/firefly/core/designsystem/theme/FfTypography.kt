/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package social.firefly.core.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val Typography =
    Typography(
        bodyLarge =
        TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
        ),
        titleLarge =
        TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp,
        ),
        labelSmall =
        TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
        ),
    )

/**
 * A custom typography for Mozilla Social for Android.
 */
class FfTypography(
    val displayLarge: TextStyle,
    val displayMedium: TextStyle,
    val displaySmall: TextStyle,
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val titleSmall: TextStyle,
    val labelLarge: TextStyle,
    val labelMedium: TextStyle,
    val labelSmall: TextStyle,
    val labelXSmall: TextStyle,
    val labelSmallLink: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,
)

val defaultTypography =
    FfTypography(
        displayLarge =
        TextStyle(
            fontSize = 54.sp,
            fontWeight = FontWeight.W400,
            letterSpacing = (-0.25).sp,
            lineHeight = 76.sp,
        ),
        displayMedium =
        TextStyle(
            fontSize = 43.sp,
            fontWeight = FontWeight.W400,
            lineHeight = 60.sp,
        ),
        displaySmall =
        TextStyle(
            fontSize = 38.sp,
            fontWeight = FontWeight.W400,
            lineHeight = 52.sp,
        ),
        titleLarge =
        TextStyle(
            fontSize = 22.sp,
            fontWeight = FontWeight.W400,
            lineHeight = 32.sp,
        ),
        titleMedium =
        TextStyle(
            fontSize = 17.sp,
            fontWeight = FontWeight.W500,
            lineHeight = 22.sp,
        ),
        titleSmall =
        TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.W500,
            letterSpacing = 0.1.sp,
            lineHeight = 20.sp,
        ),
        labelLarge =
        TextStyle(
            fontSize = 17.sp,
            fontWeight = FontWeight.W500,
            letterSpacing = 0.15.sp,
            lineHeight = 24.sp,
        ),
        labelMedium =
        TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.W500,
            letterSpacing = 0.1.sp,
            lineHeight = 20.sp,
        ),
        labelSmall =
        TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.W500,
            letterSpacing = 0.5.sp,
            lineHeight = 16.sp,
        ),
        labelSmallLink =
        TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.W500,
            letterSpacing = .5.sp,
            lineHeight = 16.sp,
        ),
        labelXSmall =
        TextStyle(
            fontSize = 11.sp,
            fontWeight = FontWeight.W500,
            letterSpacing = .5.sp,
            lineHeight = 16.sp,
        ),
        bodyLarge =
        TextStyle(
            fontSize = 17.sp,
            fontWeight = FontWeight.W400,
            lineHeight = 24.sp,
        ),
        bodyMedium =
        TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.W400,
            lineHeight = 20.sp,
        ),
        bodySmall =
        TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.W400,
            lineHeight = 16.sp,
        ),
    )

@Composable
@Preview
private fun TypographyPreview() {
    val textStyles =
        listOf(
            Pair("Display Large", defaultTypography.displayLarge),
            Pair("Display Medium", defaultTypography.displayMedium),
            Pair("Display Small", defaultTypography.displaySmall),
            Pair("Title Large", defaultTypography.titleLarge),
            Pair("Title Medium", defaultTypography.titleMedium),
            Pair("Title Small", defaultTypography.titleSmall),
            Pair("Label Large", defaultTypography.labelLarge),
            Pair("Label Medium", defaultTypography.labelMedium),
            Pair("Label Small", defaultTypography.labelSmall),
            Pair("Label Small Link", defaultTypography.labelSmallLink),
            Pair("Label X Small", defaultTypography.labelXSmall),
            Pair("Body Large", defaultTypography.bodyLarge),
            Pair("Body Medium", defaultTypography.bodyMedium),
            Pair("Body Small", defaultTypography.bodySmall),
        )

    FfTheme {
        LazyColumn(
            modifier =
            Modifier
                .background(FfTheme.colors.layer1)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            items(textStyles) { style ->
                Text(
                    text = style.first,
                    style = style.second,
                )
            }
        }
    }
}

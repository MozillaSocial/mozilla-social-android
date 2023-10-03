package org.mozilla.social.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mozilla.social.core.designsystem.R
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoTheme

@Composable
fun MoSoTopBar(
    title: String,
    icon: ImageVector? = MoSoIcons.Close,
    onIconClicked: () -> Unit,
    leftSideContent:  @Composable () -> Unit = {},
    rightSideContent: @Composable () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = { onIconClicked() },
                ) {
                    Icon(
                        icon,
                        stringResource(id = R.string.top_bar_close_content_description),
                        tint = MoSoTheme.colors.iconPrimary,
                    )
                }
            }
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = if (icon == null) 8.dp else 0.dp),
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
            )

            leftSideContent()
            Spacer(modifier = Modifier.weight(1f))
            rightSideContent()
        }
       MoSoDivider()
    }
}

@Preview
@Composable
private fun MoSoTopBarPreview() {
    MoSoTheme {
        MoSoTopBar(
            title = "test",
            onIconClicked = {},
            leftSideContent = {
                Text(text = "leftSide")
            },
            rightSideContent = {
                Text(text = "rightSide")
            }
        )
    }
}
package org.mozilla.social.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.mozilla.social.core.designsystem.R
import org.mozilla.social.core.designsystem.icon.MoSoIcons

@Composable
fun MoSoTopBar(
    title: String,
    onCloseClicked: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row {
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { onCloseClicked() },
            ) {
                Icon(
                    MoSoIcons.Close,
                    stringResource(id = R.string.top_bar_close_content_description),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
            )
        }
        Divider()
    }
}
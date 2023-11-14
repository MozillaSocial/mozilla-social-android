package org.mozilla.social.core.ui.common.account.quickview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.designsystem.theme.defaultTypography
import org.mozilla.social.core.ui.common.utils.PreviewTheme

@Composable
fun AccountQuickView(
    uiState: AccountQuickViewUiState,
    onClick: (accountId: String,) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(uiState.accountId) }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row {
                AsyncImage(
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                        .clip(CircleShape)
                        .background(MoSoTheme.colors.layer2),
                    model = uiState.avatarUrl,
                    contentDescription = null,
                )
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = uiState.displayName,
                        style = defaultTypography.bodyLarge
                    )
                    Text(
                        text = "@${uiState.webFinger}",
                        style = defaultTypography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AccountQuickViewPreview() {
    PreviewTheme {
        AccountQuickView(
            uiState = AccountQuickViewUiState(
                accountId = "",
                displayName = "name",
                webFinger = "webfinger",
                avatarUrl = "url",
                isFollowing = false,
            ),
            onClick = {}
        )
    }
}
package org.mozilla.social.core.ui.common.account.quickview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.mozilla.social.core.designsystem.theme.MoSoSpacing
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.designsystem.theme.defaultTypography
import org.mozilla.social.core.ui.common.utils.PreviewTheme

@Composable
fun AccountQuickView(
    uiState: AccountQuickViewUiState,
    modifier: Modifier = Modifier,
    buttonSlot: @Composable () -> Unit = {},
) {
    Row(modifier = modifier) {
        CircleAvatar(uiState.avatarUrl)

        Spacer(modifier = Modifier.width(MoSoSpacing.sm))

        Column {
            DisplayName(displayName = uiState.displayName)
            WebFinger(webFinger = uiState.webFinger)
        }

        Spacer(modifier = Modifier.weight(1f))

        buttonSlot()
    }
}

val CIRCLE_AVATAR_SIZE = 50.dp

@Composable
private fun DisplayName(modifier: Modifier = Modifier, displayName: String) {
    Text(
        modifier = modifier,
        text = displayName,
        style = defaultTypography.bodyLarge
    )
}

@Composable
private fun WebFinger(webFinger: String) {
    Text(
        text = "@${webFinger}",
        style = defaultTypography.bodyMedium
    )
}

@Composable
private fun CircleAvatar(avatarUrl: String, modifier: Modifier = Modifier) {
    AsyncImage(
        modifier = modifier
            .size(CIRCLE_AVATAR_SIZE)
            .clip(CircleShape)
            .background(MoSoTheme.colors.layer2),
        model = avatarUrl,
        contentDescription = null,
    )
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
        )
    }
}
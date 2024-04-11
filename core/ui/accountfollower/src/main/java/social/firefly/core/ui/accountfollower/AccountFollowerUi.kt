package social.firefly.core.ui.accountfollower

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import social.firefly.core.designsystem.utils.NoRipple
import social.firefly.core.ui.common.account.quickview.AccountQuickView
import social.firefly.core.ui.common.account.quickview.AccountQuickViewUiState
import social.firefly.core.ui.common.following.FollowStatus
import social.firefly.core.ui.common.following.FollowingButton
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.core.ui.htmlcontent.HtmlContent
import social.firefly.core.ui.htmlcontent.HtmlContentInteractions

@Composable
fun AccountFollower(
    uiState: AccountFollowerUiState,
    onButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NoRipple {
        AccountQuickView(
            modifier = modifier,
            uiState = uiState.accountQuickViewUiState,
            buttonSlot = {
                if (uiState.followButtonVisible) {
                    FollowingButton(
                        onButtonClicked = onButtonClicked,
                        followStatus = uiState.followStatus
                    )
                }
            },
            extraInfoSlot = {
                if (uiState.bioHtml.isNotBlank()) {
                    HtmlContent(
                        modifier = Modifier.padding(top = 8.dp),
                        htmlText = uiState.bioHtml,
                        maximumLineCount = 3,
                        htmlContentInteractions = object : HtmlContentInteractions {},
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun AccountFollowerPreview() {
    PreviewTheme {
        AccountFollower(
            uiState = AccountFollowerUiState(
                accountQuickViewUiState = AccountQuickViewUiState(
                    accountId = "",
                    displayName = "name",
                    webFinger = "webfinger",
                    avatarUrl = "url",
                ),
                followStatus = FollowStatus.FOLLOWING,
                bioHtml = "Engineer at mozilla",
                followButtonVisible = true,
            ),
            onButtonClicked = {},
        )
    }
}

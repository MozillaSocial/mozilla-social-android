package org.mozilla.social.core.ui.accountfollower

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mozilla.social.core.ui.common.account.quickview.AccountQuickView
import org.mozilla.social.core.ui.common.account.quickview.AccountQuickViewUiState
import org.mozilla.social.core.ui.common.button.MoSoToggleButton
import org.mozilla.social.core.ui.common.button.ToggleButtonState
import org.mozilla.social.core.ui.common.text.SmallTextLabel
import org.mozilla.social.core.ui.common.utils.PreviewTheme
import org.mozilla.social.core.ui.htmlcontent.HtmlContent
import org.mozilla.social.core.ui.htmlcontent.HtmlContentInteractions

@Composable
fun AccountFollower(
    uiState: AccountFollowerUiState,
    modifier: Modifier = Modifier,
) {
    AccountQuickView(
        modifier = modifier,
        uiState = uiState.accountQuickViewUiState,
        buttonSlot = {
            MoSoToggleButton(
                onClick = {},
                toggleState = if (uiState.isFollowing) {
                    ToggleButtonState.Secondary
                } else {
                    ToggleButtonState.Primary
                }
            ) {
                SmallTextLabel(
                    text = if (uiState.isFollowing) {
                        stringResource(id = R.string.unfollow_button)
                    } else {
                        stringResource(id = R.string.follow_button)
                    }
                )
            }
        },
        extraInfoSlot = {
            if (uiState.bioHtml.isNotBlank()) {
                HtmlContent(
                    modifier = Modifier.padding(top = 8.dp),
                    htmlText = uiState.bioHtml,
                    htmlContentInteractions = object : HtmlContentInteractions {}
                )
            }
        }
    )
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
                    isFollowing = false,
                ),
                isFollowing = true,
                bioHtml = "Engineer at mozilla"
            ),
        )
    }
}
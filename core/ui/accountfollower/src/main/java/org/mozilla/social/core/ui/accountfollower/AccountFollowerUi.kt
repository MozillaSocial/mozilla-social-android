package org.mozilla.social.core.ui.accountfollower

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mozilla.social.core.designsystem.utils.NoRipple
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
    onButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NoRipple {
        AccountQuickView(
            modifier = modifier,
            uiState = uiState.accountQuickViewUiState,
            buttonSlot = {
                MoSoToggleButton(
                    modifier = Modifier.height(32.dp),
                    onClick = { onButtonClicked() },
                    toggleState = if (uiState.isFollowing) {
                        ToggleButtonState.Secondary
                    } else {
                        ToggleButtonState.Primary
                    }
                ) {
                    SmallTextLabel(
                        text = if (uiState.isFollowing) {
                            stringResource(id = R.string.following_button)
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
                isFollowing = true,
                bioHtml = "Engineer at mozilla"
            ),
            onButtonClicked = {},
        )
    }
}
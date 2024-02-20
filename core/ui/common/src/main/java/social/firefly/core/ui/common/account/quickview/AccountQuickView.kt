package social.firefly.core.ui.common.account.quickview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.button.FfButton
import social.firefly.core.ui.common.text.LargeTextBody
import social.firefly.core.ui.common.text.MediumTextBody
import social.firefly.core.ui.common.utils.PreviewTheme

@Composable
fun AccountQuickView(
    uiState: AccountQuickViewUiState,
    modifier: Modifier = Modifier,
    buttonSlot: @Composable () -> Unit = {},
    extraInfoSlot: @Composable () -> Unit = {},
) {
    Row(modifier = modifier) {
        CircleAvatar(uiState.avatarUrl)

        Spacer(modifier = Modifier.width(FfSpacing.sm))

        Column {
            Row {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                ) {
                    LargeTextBody(
                        text = uiState.displayName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    MediumTextBody(
                        text = "@${uiState.webFinger}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Box(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    buttonSlot()
                }
            }
            extraInfoSlot()
        }
    }
}

@Composable
fun AccountQuickViewBox(
    uiState: AccountQuickViewUiState,
    modifier: Modifier = Modifier,
    buttonSlot: @Composable () -> Unit = {},
    extraInfoSlot: @Composable () -> Unit = {},
) {
    Column(modifier = modifier) {
        Row {
            CircleAvatar(
                modifier = Modifier.align(Alignment.CenterVertically),
                avatarUrl = uiState.avatarUrl,
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                buttonSlot()
            }
        }

        Spacer(modifier = Modifier.width(FfSpacing.sm))

        Column {
            LargeTextBody(
                text = uiState.displayName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            MediumTextBody(
                text = "@${uiState.webFinger}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            extraInfoSlot()
        }
    }
}

val CIRCLE_AVATAR_SIZE = 48.dp

@Composable
private fun CircleAvatar(
    avatarUrl: String,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        modifier =
        modifier
            .size(CIRCLE_AVATAR_SIZE)
            .clip(CircleShape)
            .background(FfTheme.colors.layer2),
        model = avatarUrl,
        contentDescription = null,
    )
}

@Preview
@Composable
private fun AccountQuickViewPreview() {
    PreviewTheme {
        AccountQuickView(
            uiState =
            AccountQuickViewUiState(
                accountId = "",
                displayName = "name",
                webFinger = "webfinger",
                avatarUrl = "url",
            ),
        )
    }
}

@Preview
@Composable
private fun AccountQuickViewPreview2() {
    PreviewTheme {
        AccountQuickView(
            uiState =
            AccountQuickViewUiState(
                accountId = "",
                displayName = "really long name really long name really long name",
                webFinger = "webfinger",
                avatarUrl = "url",
            ),
            buttonSlot = {
                FfButton(onClick = { /*TODO*/ }) {
                    Text(text = "button")
                }
            }
        )
    }
}

@Preview
@Composable
private fun AccountQuickViewPreview3() {
    PreviewTheme {
        AccountQuickView(
            uiState =
            AccountQuickViewUiState(
                accountId = "",
                displayName = "really long name really long name really long name",
                webFinger = "webfinger",
                avatarUrl = "url",
            ),
            buttonSlot = {
                FfButton(onClick = { /*TODO*/ }) {
                    Text(text = "button")
                }
            },
            extraInfoSlot = {
                Text(text = "this is more info")
            }
        )
    }
}

@Preview
@Composable
private fun AccountQuickViewBoxPreview() {
    PreviewTheme {
        AccountQuickViewBox(
            modifier = Modifier
                .width(240.dp)
                .padding(20.dp),
            uiState = AccountQuickViewUiState(
                accountId = "",
                displayName = "name",
                webFinger = "webfingerThatIsReallyLong@lalalalala",
                avatarUrl = "url",
            ),
            buttonSlot = {
                FfButton(onClick = { /*TODO*/ }) {
                    Text(text = "button")
                }
            },
            extraInfoSlot = {
                Text(text = "this is more info")
            }
        )
    }
}

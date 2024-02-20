package social.firefly.core.ui.notifications.cards

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import social.firefly.common.utils.StringFactory
import social.firefly.core.ui.common.button.FfButton
import social.firefly.core.ui.common.button.FfButtonContentPadding
import social.firefly.core.ui.common.button.FfButtonSecondary
import social.firefly.core.ui.common.text.SmallTextLabel
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.core.ui.notifications.NotificationCard
import social.firefly.core.ui.notifications.NotificationInteractions
import social.firefly.core.ui.notifications.NotificationInteractionsNoOp
import social.firefly.core.ui.notifications.NotificationUiState
import social.firefly.core.ui.notifications.R
import social.firefly.core.ui.postcard.PostCardInteractions

@Composable
internal fun FollowRequestNotificationContent(
    uiState: NotificationUiState.FollowRequest,
    notificationInteractions: NotificationInteractions,
) {
    Row {
        FfButtonSecondary(
            modifier = Modifier
                .weight(1f),
            onClick = {
                notificationInteractions.onDenyFollowRequestClicked(
                    accountId = uiState.accountId,
                    notificationId = uiState.id,
                )
            },
            contentPadding = FfButtonContentPadding.small,
        ) {
            SmallTextLabel(text = stringResource(id = R.string.deny_follow_request))
        }

        Spacer(modifier = Modifier.width(16.dp))

        FfButton(
            modifier = Modifier
                .weight(1f),
            onClick = {
                notificationInteractions.onAcceptFollowRequestClicked(
                    accountId = uiState.accountId,
                    notificationId = uiState.id,
                )
            },
            contentPadding = FfButtonContentPadding.small,
        ) {
            SmallTextLabel(text = stringResource(id = R.string.accept_follow_request))
        }

        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Preview
@Composable
private fun FollowRequestNotificationPreview() {
    PreviewTheme {
        NotificationCard(
            uiState = NotificationUiState.FollowRequest(
                id = 1,
                timeStamp = StringFactory.literal("1 day ago"),
                title = StringFactory.literal("John mentioned you:"),
                avatarUrl = "",
                accountId = "",
                accountName = "",
            ),
            postCardInteractions = object : PostCardInteractions {},
            notificationInteractions = NotificationInteractionsNoOp,
        )
    }
}
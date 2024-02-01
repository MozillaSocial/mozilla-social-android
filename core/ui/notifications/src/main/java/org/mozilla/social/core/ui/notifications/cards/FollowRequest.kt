package org.mozilla.social.core.ui.notifications.cards

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.ui.common.button.MoSoButton
import org.mozilla.social.core.ui.common.button.MoSoButtonContentPadding
import org.mozilla.social.core.ui.common.button.MoSoButtonSecondary
import org.mozilla.social.core.ui.common.text.SmallTextLabel
import org.mozilla.social.core.ui.common.utils.PreviewTheme
import org.mozilla.social.core.ui.notifications.NotificationCard
import org.mozilla.social.core.ui.notifications.NotificationInteractions
import org.mozilla.social.core.ui.notifications.NotificationInteractionsNoOp
import org.mozilla.social.core.ui.notifications.NotificationUiState
import org.mozilla.social.core.ui.notifications.R
import org.mozilla.social.core.ui.postcard.PostCardInteractions

@Composable
internal fun FollowRequestNotificationContent(
    uiState: NotificationUiState.FollowRequest,
    notificationInteractions: NotificationInteractions,
) {
    Row {
        MoSoButtonSecondary(
            modifier = Modifier
                .weight(1f),
            onClick = {
                notificationInteractions.onDenyFollowRequestClicked(
                    accountId = uiState.accountId,
                    notificationId = uiState.id,
                )
            },
            contentPadding = MoSoButtonContentPadding.small,
        ) {
            SmallTextLabel(text = stringResource(id = R.string.deny_follow_request))
        }

        Spacer(modifier = Modifier.width(16.dp))

        MoSoButton(
            modifier = Modifier
                .weight(1f),
            onClick = {
                notificationInteractions.onAcceptFollowRequestClicked(
                    accountId = uiState.accountId,
                    notificationId = uiState.id,
                )
            },
            contentPadding = MoSoButtonContentPadding.small,
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
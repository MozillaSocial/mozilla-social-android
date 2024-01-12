package org.mozilla.social.core.ui.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.common.text.MediumTextLabel
import org.mozilla.social.core.ui.common.text.SmallTextLabel
import org.mozilla.social.core.ui.htmlcontent.HtmlContentInteractions
import org.mozilla.social.core.ui.notifications.cards.FavoriteNotification
import org.mozilla.social.core.ui.notifications.cards.FollowNotification
import org.mozilla.social.core.ui.notifications.cards.FollowRequestNotification
import org.mozilla.social.core.ui.notifications.cards.MentionNotification
import org.mozilla.social.core.ui.notifications.cards.NewStatusNotification
import org.mozilla.social.core.ui.notifications.cards.PollEndedNotification
import org.mozilla.social.core.ui.notifications.cards.RepostNotification
import org.mozilla.social.core.ui.notifications.cards.StatusUpdatedNotification
import org.mozilla.social.core.ui.poll.PollInteractions
import org.mozilla.social.core.ui.postcard.PostContent

@Composable
fun NotificationCard(
    uiState: NotificationUiState,
    htmlContentInteractions: HtmlContentInteractions,
    pollInteractions: PollInteractions,
    notificationInteractions: NotificationInteractions,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (uiState) {
            is NotificationUiState.Favorite -> FavoriteNotification(uiState = uiState)
            is NotificationUiState.Follow -> FollowNotification(uiState = uiState)
            is NotificationUiState.FollowRequest -> FollowRequestNotification(uiState = uiState)
            is NotificationUiState.Mention -> MentionNotification(
                uiState = uiState,
                htmlContentInteractions = htmlContentInteractions,
                pollInteractions = pollInteractions,
                notificationInteractions = notificationInteractions,
            )

            is NotificationUiState.NewStatus -> NewStatusNotification(uiState = uiState)
            is NotificationUiState.PollEnded -> PollEndedNotification(uiState = uiState)
            is NotificationUiState.Repost -> RepostNotification(uiState = uiState)
            is NotificationUiState.StatusUpdated -> StatusUpdatedNotification(uiState = uiState)
        }
    }
}

@Composable
internal fun NotificationCard(
    uiState: NotificationUiState,
    notificationInteractions: NotificationInteractions,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier,
    ) {
        Avatar(
            avatarUrl = uiState.avatarUrl,
            size = 36.dp,
            accountId = uiState.accountId,
            notificationInteractions = notificationInteractions,
        )
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Column {
            NotificationMetaData(
                modifier = Modifier
                    .height(36.dp),
                uiState = uiState,
            )
            content()
        }
    }
}

@Composable
private fun NotificationMetaData(
    uiState: NotificationUiState,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Row(
        modifier = modifier,
    ) {
        MediumTextLabel(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            text = uiState.title.build(context)
        )
        SmallTextLabel(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = uiState.timeStamp.build(context),
            color = MoSoTheme.colors.textSecondary,
        )
    }
}

@Composable
private fun Avatar(
    modifier: Modifier = Modifier,
    avatarUrl: String,
    size: Dp,
    accountId: String,
    notificationInteractions: NotificationInteractions,
) {
    AsyncImage(
        modifier =
        modifier
            .size(size)
            .clip(CircleShape)
            .background(MoSoTheme.colors.layer2)
            .clickable { notificationInteractions.onAvatarClicked(accountId) },
        model = avatarUrl,
        contentDescription = "",
    )
}
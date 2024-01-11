package org.mozilla.social.core.ui.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
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

@Composable
fun NotificationCard(
    uiState: NotificationUiState,
    htmlContentInteractions: HtmlContentInteractions,
    pollInteractions: PollInteractions,
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
            )

            is NotificationUiState.NewStatus -> NewStatusNotification(uiState = uiState)
            is NotificationUiState.PollEnded -> PollEndedNotification(uiState = uiState)
            is NotificationUiState.Repost -> RepostNotification(uiState = uiState)
            is NotificationUiState.StatusUpdated -> StatusUpdatedNotification(uiState = uiState)
        }
    }
}

@Composable
internal fun NotificationMetaData(
    uiState: NotificationUiState,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Row(
        modifier = modifier,
    ) {
        MediumTextLabel(
            modifier = Modifier.weight(1f),
            text = uiState.title.build(context)
        )
        SmallTextLabel(
            text = uiState.timeStamp.build(context),
            color = MoSoTheme.colors.textSecondary,
        )
    }
}

@Composable
internal fun Avatar(
    modifier: Modifier = Modifier,
    avatarUrl: String,
) {
    AsyncImage(
        modifier =
        modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(MoSoTheme.colors.layer2)
            .clickable { },
        model = avatarUrl,
        contentDescription = "",
    )
}
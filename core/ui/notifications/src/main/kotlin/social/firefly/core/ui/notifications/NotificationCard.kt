package social.firefly.core.ui.notifications

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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.designsystem.utils.NoRipple
import social.firefly.core.ui.common.text.MediumTextLabel
import social.firefly.core.ui.common.text.SmallTextLabel
import social.firefly.core.ui.notifications.cards.FavoriteNotificationContent
import social.firefly.core.ui.notifications.cards.FollowRequestNotificationContent
import social.firefly.core.ui.notifications.cards.MentionNotificationContent
import social.firefly.core.ui.notifications.cards.NewStatusNotificationContent
import social.firefly.core.ui.notifications.cards.PollEndedNotificationContent
import social.firefly.core.ui.notifications.cards.RepostNotificationContent
import social.firefly.core.ui.notifications.cards.StatusUpdatedNotificationContent
import social.firefly.core.ui.postcard.PostCardInteractions

@Suppress("LongMethod")
@Composable
fun NotificationCard(
    uiState: NotificationUiState,
    postCardInteractions: PostCardInteractions,
    notificationInteractions: NotificationInteractions,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (uiState) {
            is NotificationUiState.Favorite -> NotificationCard(
                modifier = Modifier.clickable {
                    notificationInteractions.onFavoritedCardClicked(uiState.statusId)
                },
                uiState = uiState,
                notificationInteractions = notificationInteractions,
                notificationTypeIcon = FfIcons.heart(),
            ) {
                FavoriteNotificationContent(
                    uiState = uiState,
                    postCardInteractions = postCardInteractions,
                )
            }

            is NotificationUiState.Follow -> NotificationCard(
                modifier = Modifier.clickable {
                    notificationInteractions.onFollowCardClicked(uiState.accountId)
                },
                uiState = uiState,
                notificationInteractions = notificationInteractions,
                notificationTypeIcon = FfIcons.following(),
            ) {}

            is NotificationUiState.FollowRequest -> NotificationCard(
                modifier = Modifier.clickable {
                    notificationInteractions.onFollowRequestCardClicked(uiState.accountId)
                },
                uiState = uiState,
                notificationInteractions = notificationInteractions,
                notificationTypeIcon = FfIcons.users(),
            ) {
                FollowRequestNotificationContent(
                    uiState = uiState,
                    notificationInteractions = notificationInteractions,
                )
            }

            is NotificationUiState.Mention -> NotificationCard(
                modifier = Modifier.clickable {
                    notificationInteractions.onMentionClicked(uiState.statusId)
                },
                uiState = uiState,
                notificationInteractions = notificationInteractions,
                notificationTypeIcon = FfIcons.at(),
            ) {
                MentionNotificationContent(
                    uiState = uiState,
                    postCardInteractions = postCardInteractions,
                )
            }

            is NotificationUiState.NewStatus -> NotificationCard(
                modifier = Modifier.clickable {
                    notificationInteractions.onNewStatusClicked(uiState.statusId)
                },
                uiState = uiState,
                notificationInteractions = notificationInteractions,
                notificationTypeIcon = FfIcons.bell(),
            ) {
                NewStatusNotificationContent(
                    uiState = uiState,
                    postCardInteractions = postCardInteractions,
                )
            }

            is NotificationUiState.PollEnded -> NotificationCard(
                modifier = Modifier.clickable {
                    notificationInteractions.onPollEndedClicked(uiState.statusId)
                },
                uiState = uiState,
                notificationInteractions = notificationInteractions,
                notificationTypeIcon = FfIcons.chartBar(),
            ) {
                PollEndedNotificationContent(
                    uiState = uiState,
                    postCardInteractions = postCardInteractions,
                )
            }

            is NotificationUiState.Repost -> NotificationCard(
                modifier = Modifier.clickable {
                    notificationInteractions.onRepostClicked(uiState.statusId)
                },
                uiState = uiState,
                notificationInteractions = notificationInteractions,
                notificationTypeIcon = FfIcons.boost(),
            ) {
                RepostNotificationContent(
                    uiState = uiState,
                    postCardInteractions = postCardInteractions,
                )
            }

            is NotificationUiState.StatusUpdated -> NotificationCard(
                modifier = Modifier.clickable {
                    notificationInteractions.onStatusUpdatedCardClicked(uiState.statusId)
                },
                uiState = uiState,
                notificationInteractions = notificationInteractions,
                notificationTypeIcon = FfIcons.bell(),
            ) {
                StatusUpdatedNotificationContent(
                    uiState = uiState,
                    postCardInteractions = postCardInteractions,
                )
            }
        }
    }
}

@Composable
private fun NotificationCard(
    uiState: NotificationUiState,
    notificationInteractions: NotificationInteractions,
    notificationTypeIcon: Painter,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    NoRipple {
        Row(
            modifier = modifier,
        ) {
            Avatar(
                avatarUrl = uiState.avatarUrl,
                size = 48.dp,
                accountId = uiState.accountId,
                accountName = uiState.accountName,
                notificationTypeIcon = notificationTypeIcon,
                notificationInteractions = notificationInteractions,
            )
            Spacer(modifier = Modifier.padding(start = 8.dp))
            Column {
                NotificationMetaData(
                    modifier = Modifier
                        .height(48.dp),
                    uiState = uiState,
                )
                content()
            }
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
        modifier = modifier
            .semantics(mergeDescendants = true) { },
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
            color = FfTheme.colors.textSecondary,
        )
    }
}

@Composable
private fun Avatar(
    modifier: Modifier = Modifier,
    avatarUrl: String,
    size: Dp,
    accountId: String,
    accountName: String,
    notificationTypeIcon: Painter,
    notificationInteractions: NotificationInteractions,
) {
    Box(
        modifier = modifier
            .size(size)
            .clickable(
                onClickLabel = stringResource(id = R.string.view_account_of_user, accountName)
            ) { notificationInteractions.onAvatarClicked(accountId) }
            .semantics(mergeDescendants = true) {
                contentDescription = accountName
            }
    ) {
        AsyncImage(
            modifier = Modifier
                .size(36.dp)
                .align(Alignment.CenterEnd)
                .clip(CircleShape)
                .background(FfTheme.colors.layer2),
            model = avatarUrl,
            contentDescription = "",
        )
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(FfTheme.colors.layer1),
        ) {
            Icon(
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.Center),
                painter = notificationTypeIcon,
                contentDescription = "",
                tint = FfTheme.colors.iconAccent,
            )
        }
    }
}

package org.mozilla.social.core.ui.postcard

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import kotlinx.datetime.Instant
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.common.utils.timeSinceNow
import org.mozilla.social.core.designsystem.component.MoSoDropdownMenu
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.FirefoxColor
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.DropDownItem
import org.mozilla.social.core.ui.R
import org.mozilla.social.core.ui.media.MediaDisplay
import org.mozilla.social.core.ui.poll.Poll
import org.mozilla.social.core.ui.htmlcontent.HtmlContent

@Composable
fun PostCard(
    post: PostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    Column(
        Modifier
            .padding(8.dp)
            .fillMaxSize()
            .clickable { postCardInteractions.onPostCardClicked(post.mainPostCardUiState.statusId) }
    ) {
        post.topRowMetaDataUiState?.let { TopRowMetaData(it) }
        MainPost(post.mainPostCardUiState, postCardInteractions)
    }
}

@Composable
private fun TopRowMetaData(
    topRowMetaDataUiState: TopRowMetaDataUiState,
) {
    Row(
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Icon(
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.CenterVertically),
            imageVector = topRowMetaDataUiState.icon,
            contentDescription = ""
        )
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = topRowMetaDataUiState.text.build(LocalContext.current),
            fontSize = 14.sp
        )
    }
}

@Composable
private fun MainPost(
    post: MainPostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    MetaData(
        post = post,
        postCardInteractions = postCardInteractions,
    )
    HtmlContent(
        mentions = post.mentions,
        htmlText = post.statusTextHtml,
        htmlContentInteractions = postCardInteractions,
    )
    Spacer(modifier = Modifier.padding(top = 4.dp))
    MediaDisplay(attachments = post.mediaAttachments)
    post.pollUiState?.let { Poll(it, postCardInteractions) }

    BottomRow(post, postCardInteractions)
}

@Composable
private fun MetaData(
    post: MainPostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    val overflowMenuExpanded = remember { mutableStateOf(false) }

    val context = LocalContext.current

    Row {
        AsyncImage(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .align(Alignment.CenterVertically)
                .background(MoSoTheme.colors.layer2)
                .clickable { postCardInteractions.onAccountImageClicked(post.accountId) },
            model = post.profilePictureUrl,
            contentDescription = "",
        )
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Column(
            modifier = Modifier.align(Alignment.CenterVertically),
        ) {
            Text(
                text = post.username,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${post.postTimeSince.build(context)} - @${post.accountName.build(context)}",
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            modifier = Modifier.width(IntrinsicSize.Max),
            onClick = { overflowMenuExpanded.value = true }
        ) {
            Icon(imageVector = MoSoIcons.MoreVert, contentDescription = "")

            MoSoDropdownMenu(
                expanded = overflowMenuExpanded.value,
                onDismissRequest = {
                    overflowMenuExpanded.value = false
                }
            ) {
                DropDownItem(
                    text = stringResource(id = R.string.mute_user, post.username),
                    expanded = overflowMenuExpanded,
                    onClick = { postCardInteractions.onOverflowMuteClicked(post.accountId) }
                )
                DropDownItem(
                    text = stringResource(id = R.string.block_user, post.username),
                    expanded = overflowMenuExpanded,
                    onClick = { postCardInteractions.onOverflowBlockClicked(post.accountId) }
                )
                DropDownItem(
                    text = stringResource(id = R.string.report_user, post.username),
                    expanded = overflowMenuExpanded,
                    onClick = { postCardInteractions.onOverflowReportClicked(post.accountId, post.statusId) }
                )
            }
        }
    }
}

@Composable
private fun BottomRow(
    post: MainPostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        BottomIconButton(
            onClick = { postCardInteractions.onReplyClicked(post.statusId) },
            imageVector = MoSoIcons.Reply,
            count = post.replyCount,
        )
        Spacer(modifier = Modifier.weight(1f))
        BottomIconButton(
            onClick = { postCardInteractions.onBoostClicked(post.statusId, !post.userBoosted) },
            imageVector = MoSoIcons.Repeat,
            count = post.boostCount,
            highlighted = post.userBoosted,
        )
        Spacer(modifier = Modifier.weight(1f))
        BottomIconButton(
            onClick = { postCardInteractions.onFavoriteClicked(post.statusId, !post.isFavorited) },
            imageVector = MoSoIcons.StarBorder,
            count = post.favoriteCount,
            highlighted = post.isFavorited,
            highlightColor = FirefoxColor.Yellow40
        )
        Spacer(modifier = Modifier.weight(1f))
        BottomIconButton(
            onClick = {
                post.url?.let { url ->
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, url)
                        type = "text/plain"
                    }

                    startActivity(context, Intent.createChooser(sendIntent, null), null)
                }
            },
            imageVector = MoSoIcons.Share,
            count = 0,
        )
    }
}

@Composable
private fun BottomIconButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    count: Long,
    highlighted: Boolean = false,
    highlightColor: Color = MaterialTheme.colorScheme.primary,
) {
    IconButton(
        modifier = Modifier.width(IntrinsicSize.Max),
        onClick = onClick
    ) {
        Row {
            Icon(
                imageVector = imageVector,
                "",
                tint = if (highlighted) {
                    highlightColor
                } else {
                    LocalContentColor.current
                }
            )
            if (count > 0) {
                Text(text = "$count")
            }
        }
    }
}

@Suppress("MagicNumber", "MaxLineLength")
@Preview
@Composable
private fun PostCardPreview() {
    MoSoTheme {
        MoSoSurface {
            PostCard(
                post = PostCardUiState(
                    statusId = "",
                    topRowMetaDataUiState = TopRowMetaDataUiState(
                        MoSoIcons.Reply,
                        StringFactory.literal("")
                    ),
                    mainPostCardUiState = MainPostCardUiState(
                        url = "",
                        pollUiState = null,
                        username = "Cool guy",
                        statusTextHtml = "<p><span class=\"h-card\"><a href=\"https://mozilla.social/@obez\" class=\"u-url mention\" rel=\"nofollow noopener noreferrer\" target=\"_blank\">@<span>obez</span></a></span> This is a text status.  Here is the text and that is all I have to say about that.</p>",
                        mediaAttachments = emptyList(),
                        profilePictureUrl = "",
                        postTimeSince = Instant.fromEpochMilliseconds(1695308821000L).timeSinceNow(),
                        accountName = StringFactory.literal("coolguy"),
                        replyCount = 4L,
                        boostCount = 3L,
                        favoriteCount = 7L,
                        statusId = "",
                        userBoosted = false,
                        isFavorited = false,
                        accountId = "",
                        mentions = emptyList()
                    )
                ),
                postCardInteractions = object : PostCardInteractions {},
            )
        }
    }
}
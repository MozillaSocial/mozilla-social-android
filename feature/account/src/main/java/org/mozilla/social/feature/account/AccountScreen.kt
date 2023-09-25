package org.mozilla.social.feature.account

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.designsystem.component.MoSoDivider
import org.mozilla.social.core.designsystem.component.MoSoTopBar
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.DropDownItem
import org.mozilla.social.core.ui.htmlcontent.HtmlContent
import org.mozilla.social.core.ui.htmlcontent.HtmlContentInteractions
import org.mozilla.social.core.ui.postcard.PostCardInteractions
import org.mozilla.social.core.ui.postcard.PostCardList
import org.mozilla.social.core.ui.postcard.PostCardNavigation
import org.mozilla.social.core.ui.postcard.PostCardUiState
import org.mozilla.social.model.Account

@Composable
fun AccountScreen(
    accountId: String?,
    onFollowingClicked: () -> Unit,
    onFollowersClicked: () -> Unit,
    onCloseClicked: () -> Unit = {},
    postCardNavigation: PostCardNavigation,
    viewModel: AccountViewModel = koinViewModel(
        parameters = {
            parametersOf(
                accountId,
                postCardNavigation,
            )
        }
    ),
) {
    val account = viewModel.account.collectAsState(initial = null).value

    account?.let {
        AccountScreen(
            account = it,
            showTopBar = viewModel.shouldShowTopBar,
            isUsersProfile = viewModel.isUsersProfile,
            feed = viewModel.feed,
            errorToastMessage = viewModel.postCardDelegate.errorToastMessage,
            onFollowingClicked = onFollowingClicked,
            onFollowersClicked = onFollowersClicked,
            onCloseClicked = onCloseClicked,
            htmlContentInteractions = viewModel.postCardDelegate,
            postCardInteractions = viewModel.postCardDelegate,
        )
    }
}

@Composable
internal fun AccountScreen(
    account: Account,
    showTopBar: Boolean,
    isUsersProfile: Boolean,
    feed: Flow<PagingData<PostCardUiState>>,
    errorToastMessage: SharedFlow<StringFactory>,
    onFollowingClicked: () -> Unit,
    onFollowersClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    htmlContentInteractions: HtmlContentInteractions,
    postCardInteractions: PostCardInteractions,
) {
    Column {
        if (showTopBar) {
            MoSoTopBar(
                title = account.username,
                onIconClicked = onCloseClicked,
                rightSideContent = {
                    if (!isUsersProfile) {
                        OverflowMenu(
                            account = account,
                            postCardInteractions = postCardInteractions,
                        )
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {

            PostCardList(
                feed = feed,
                errorToastMessage = errorToastMessage,
                postCardInteractions = postCardInteractions
            ) {
                HeaderAndProfileImages(
                    headerImage = account.headerUrl,
                    headerStaticUrl = account.headerStaticUrl,
                    profileImage = account.avatarUrl,
                    profileStaticUrl = account.avatarStaticUrl,
                    onHeaderClick = { /*TODO*/ },
                    onProfileClick = { /*TODO*/ }
                )

                UserInfo(account = account)
                UserFollow(
                    account = account,
                    onFollowingClicked = onFollowingClicked,
                    onFollowersClicked = onFollowersClicked,
                )
                UserBio(
                    account = account,
                    htmlContentInteractions = htmlContentInteractions,
                )
                Spacer(modifier = Modifier.padding(top = 4.dp))
                UserFields(
                    account = account,
                    htmlContentInteractions = htmlContentInteractions,
                )
                QuickFunctions(
                    name = R.string.posts,
                    numericalValue = account.statusesCount,
                    onClick = { /*TODO*/ }
                )
                MoSoDivider()
            }
        }
    }
}

@Composable
private fun OverflowMenu(
    account: Account,
    postCardInteractions: PostCardInteractions,
) {
    val overflowMenuExpanded = remember { mutableStateOf(false) }
    IconButton(
        modifier = Modifier.width(IntrinsicSize.Max),
        onClick = { overflowMenuExpanded.value = true }
    ) {
        Icon(imageVector = MoSoIcons.MoreVert, contentDescription = null)

        DropdownMenu(
            expanded = overflowMenuExpanded.value,
            onDismissRequest = {
                overflowMenuExpanded.value = false
            }
        ) {
            DropDownItem(
                text = stringResource(
                    id = org.mozilla.social.core.ui.R.string.mute_user,
                    account.username
                ),
                expanded = overflowMenuExpanded,
                onClick = { postCardInteractions.onOverflowMuteClicked(account.accountId) }
            )
            DropDownItem(
                text = stringResource(
                    id = org.mozilla.social.core.ui.R.string.block_user,
                    account.username
                ),
                expanded = overflowMenuExpanded,
                onClick = { postCardInteractions.onOverflowBlockClicked(account.accountId) }
            )
            DropDownItem(
                text = stringResource(
                    id = org.mozilla.social.core.ui.R.string.report_user,
                    account.username
                ),
                expanded = overflowMenuExpanded,
                onClick = { postCardInteractions.onOverflowReportClicked(account.accountId) }
            )
        }
    }
}

@Composable
private fun UserInfo(
    account: Account,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = account.displayName,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(bottom = 4.dp)
        )
        Text(text = "@${account.acct}")
    }
}

@Composable
private fun UserFollow(
    account: Account,
    onFollowingClicked: () -> Unit,
    onFollowersClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .border(
                border = BorderStroke(2.dp, Color.Gray),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                .clickable { onFollowersClicked() },
            text = stringResource(id = R.string.followers_count, account.followersCount),
        )
        MoSoDivider(
            color = Color.Gray,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                .clickable { onFollowingClicked() },
            text = stringResource(id = R.string.following_count, account.followingCount),

            )
    }
}

@Composable
private fun UserBio(
    modifier: Modifier = Modifier,
    account: Account,
    htmlContentInteractions: HtmlContentInteractions,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
    ) {
        HtmlContent(
            mentions = emptyList(),
            htmlText = account.bio,
            htmlContentInteractions = htmlContentInteractions
        )
    }
}

@Composable
private fun UserFields(
    account: Account,
    htmlContentInteractions: HtmlContentInteractions,
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .width(IntrinsicSize.Max)
            .padding(start = 8.dp, end = 8.dp)
            .border(
                border = BorderStroke(2.dp, Color.Gray),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        account.fields?.forEachIndexed { index, field ->
            Column(
                modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    text = field.name,
                    style = MaterialTheme.typography.bodyMedium
                )
                HtmlContent(
                    mentions = emptyList(),
                    htmlText = field.value,
                    htmlContentInteractions = htmlContentInteractions,
                )
            }
            if (index < (account.fields?.size ?: 0) - 1) {
                MoSoDivider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(1.dp)
                )
            }
        }
    }
}

@Composable
private fun QuickFunctions(
    modifier: Modifier = Modifier,
    @StringRes name: Int,
    numericalValue: Any,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                border = BorderStroke(2.dp, Color.Gray),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
    ) {
        Text(
            text = stringResource(id = name),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                .align(Alignment.CenterStart)
        )
        Text(
            text = numericalValue.toString(),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
        )
    }
}

@Composable
private fun OverlapObjects(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->
        val largeBox = measurables[0]
        val smallBox = measurables[1]
        val looseConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
        )
        val largePlaceable = largeBox.measure(looseConstraints)
        val smallPlaceable = smallBox.measure(looseConstraints)
        layout(
            width = constraints.maxWidth,
            height = largePlaceable.height + smallPlaceable.height / 2,
        ) {
            largePlaceable.placeRelative(
                x = 0,
                y = 0,
            )
            smallPlaceable.placeRelative(
                x = (constraints.maxWidth - smallPlaceable.width) / 2,
                y = largePlaceable.height - smallPlaceable.height / 2
            )
        }
    }
}

@Composable
private fun HeaderAndProfileImages(
    modifier: Modifier = Modifier,
    headerImage: String,
    headerStaticUrl: String,
    profileImage: String,
    profileStaticUrl: String,
    onHeaderClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    OverlapObjects(modifier = modifier.fillMaxWidth()) {
        val scrollState = rememberScrollState()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onHeaderClick() }
                .graphicsLayer {
                    alpha = 1f - ((scrollState.value.toFloat() / scrollState.maxValue) * 1.5f)
                    translationY = 0.5f * scrollState.value
                },
        ) {
            AsyncImage(
                model = headerImage,
                contentDescription = headerStaticUrl,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
        }
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { onProfileClick() }
        ) {
            AsyncImage(
                model = profileImage,
                contentDescription = profileStaticUrl,
                modifier = Modifier
                    .clip(CircleShape)
            )
        }
    }
}

@Preview
@Composable
fun AccountScreenPreview() {
    MoSoTheme {
//        AccountScreen("110810174933375392")
    }
}
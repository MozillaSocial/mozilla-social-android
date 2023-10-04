package org.mozilla.social.feature.account

import android.content.Intent
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.paging.PagingData
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.common.Resource
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.designsystem.component.MoSoButton
import org.mozilla.social.core.designsystem.component.MoSoCircularProgressIndicator
import org.mozilla.social.core.designsystem.component.MoSoDivider
import org.mozilla.social.core.designsystem.component.MoSoDropdownMenu
import org.mozilla.social.core.designsystem.component.MoSoToast
import org.mozilla.social.core.designsystem.component.MoSoTopBar
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.DropDownItem
import org.mozilla.social.core.ui.error.GenericError
import org.mozilla.social.core.ui.htmlcontent.HtmlContent
import org.mozilla.social.core.ui.htmlcontent.HtmlContentInteractions
import org.mozilla.social.core.ui.postcard.PostCardInteractions
import org.mozilla.social.core.ui.postcard.PostCardList
import org.mozilla.social.core.ui.postcard.PostCardNavigation
import org.mozilla.social.core.ui.postcard.PostCardUiState

@Composable
internal fun AccountScreen(
    accountId: String?,
    accountNavigationCallbacks: AccountNavigationCallbacks,
    postCardNavigation: PostCardNavigation,
    viewModel: AccountViewModel = koinViewModel(
        parameters = {
            parametersOf(
                accountId,
                postCardNavigation,
                accountNavigationCallbacks,
            )
        }
    ),
) {

    AccountScreen(
        resource = viewModel.uiState.collectAsState().value,
        closeButtonVisible = viewModel.shouldShowCloseButton,
        isUsersProfile = viewModel.isOwnProfile,
        feed = viewModel.feed,
        errorToastMessage = viewModel.postCardDelegate.errorToastMessage,
        accountNavigationCallbacks = accountNavigationCallbacks,
        htmlContentInteractions = viewModel.postCardDelegate,
        postCardInteractions = viewModel.postCardDelegate,
        accountInteractions = viewModel
    )

    MoSoToast(toastMessage = viewModel.errorToastMessage)
}

@Composable
private fun AccountScreen(
    resource: Resource<AccountUiState>,
    closeButtonVisible: Boolean,
    isUsersProfile: Boolean,
    feed: Flow<PagingData<PostCardUiState>>,
    errorToastMessage: SharedFlow<StringFactory>,
    accountNavigationCallbacks: AccountNavigationCallbacks,
    htmlContentInteractions: HtmlContentInteractions,
    postCardInteractions: PostCardInteractions,
    accountInteractions: AccountInteractions,
) {
    Column {
        when (resource) {
            is Resource.Loading -> {
                MoSoTopBar(
                    icon = if (closeButtonVisible) {
                        MoSoIcons.Close
                    } else {
                        null
                    },
                    onIconClicked = { accountNavigationCallbacks.onCloseClicked() },
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(
                            align = Alignment.Center
                        )
                ) {
                    MoSoCircularProgressIndicator()
                }
            }
            is Resource.Loaded<AccountUiState> -> {
                MoSoTopBar(
                    title = resource.data.displayName,
                    icon = if (closeButtonVisible) {
                        MoSoIcons.Close
                    } else {
                        null
                    },
                    onIconClicked = { accountNavigationCallbacks.onCloseClicked() },
                    rightSideContent = {
                        OverflowMenu(
                            account = resource.data,
                            isUsersProfile = isUsersProfile,
                            overflowInteractions = accountInteractions,
                        )
                    }
                )

                MainContent(
                    account = resource.data,
                    isUsersProfile = isUsersProfile,
                    feed = feed,
                    errorToastMessage = errorToastMessage,
                    htmlContentInteractions = htmlContentInteractions,
                    postCardInteractions = postCardInteractions,
                    accountInteractions = accountInteractions
                )
            }
            is Resource.Error -> {
                MoSoTopBar(
                    icon = if (closeButtonVisible) {
                        MoSoIcons.Close
                    } else {
                        null
                    },
                    onIconClicked = { accountNavigationCallbacks.onCloseClicked() },
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(
                            align = Alignment.Center
                        )
                ) {
                    GenericError(
                        onRetryClicked = {
                            accountInteractions.onRetryClicked()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MainContent(
    account: AccountUiState,
    isUsersProfile: Boolean,
    feed: Flow<PagingData<PostCardUiState>>,
    errorToastMessage: SharedFlow<StringFactory>,
    htmlContentInteractions: HtmlContentInteractions,
    postCardInteractions: PostCardInteractions,
    accountInteractions: AccountInteractions,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {

        PostCardList(
            feed = feed,
            errorToastMessage = errorToastMessage,
            postCardInteractions = postCardInteractions
        ) {
            Header(
                isUsersProfile = isUsersProfile,
                accountUiState = account,
                accountInteractions = accountInteractions,
            )

            UserInfo(account = account)
            UserFollow(
                account = account,
                accountInteractions = accountInteractions,
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

@Composable
private fun OverflowMenu(
    account: AccountUiState,
    isUsersProfile: Boolean,
    overflowInteractions: OverflowInteractions,
) {
    val overflowMenuExpanded = remember { mutableStateOf(false) }
    val context = LocalContext.current
    IconButton(
        modifier = Modifier.width(IntrinsicSize.Max),
        onClick = { overflowMenuExpanded.value = true }
    ) {
        Icon(imageVector = MoSoIcons.MoreVert, contentDescription = null)

        MoSoDropdownMenu(
            expanded = overflowMenuExpanded.value,
            onDismissRequest = {
                overflowMenuExpanded.value = false
            }
        ) {
            DropDownItem(
                text = stringResource(R.string.share_option),
                expanded = overflowMenuExpanded,
                onClick = {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, account.accountUrl)
                        type = "text/plain"
                    }

                    ContextCompat.startActivity(
                        context,
                        Intent.createChooser(sendIntent, null),
                        null
                    )
                }
            )
            if (!isUsersProfile) {
                if (account.isMuted) {
                    DropDownItem(
                        text = stringResource(
                            id = org.mozilla.social.core.ui.R.string.unmute_user,
                            account.username
                        ),
                        expanded = overflowMenuExpanded,
                        onClick = { overflowInteractions.onOverflowUnmuteClicked() }
                    )
                } else {
                    DropDownItem(
                        text = stringResource(
                            id = org.mozilla.social.core.ui.R.string.mute_user,
                            account.username
                        ),
                        expanded = overflowMenuExpanded,
                        onClick = { overflowInteractions.onOverflowMuteClicked() }
                    )
                }

                if (account.isBlocked) {
                    DropDownItem(
                        text = stringResource(
                            id = org.mozilla.social.core.ui.R.string.unblock_user,
                            account.username
                        ),
                        expanded = overflowMenuExpanded,
                        onClick = { overflowInteractions.onOverflowUnblockClicked() }
                    )
                } else {
                    DropDownItem(
                        text = stringResource(
                            id = org.mozilla.social.core.ui.R.string.block_user,
                            account.username
                        ),
                        expanded = overflowMenuExpanded,
                        onClick = { overflowInteractions.onOverflowBlockClicked() }
                    )
                }

                DropDownItem(
                    text = stringResource(
                        id = org.mozilla.social.core.ui.R.string.report_user,
                        account.username
                    ),
                    expanded = overflowMenuExpanded,
                    onClick = { overflowInteractions.onOverflowReportClicked() }
                )
            }
        }
    }
}

@Composable
private fun UserInfo(
    account: AccountUiState,
) {
    Column(
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = account.displayName,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(bottom = 4.dp)
        )
        Text(text = "@${account.webFinger}")
    }
}

@Composable
private fun UserFollow(
    account: AccountUiState,
    accountInteractions: AccountInteractions,
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(8.dp)
            .border(
                border = BorderStroke(2.dp, Color.Gray),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                .clickable { accountInteractions.onFollowersClicked() },
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
                .clickable { accountInteractions.onFollowingClicked() },
            text = stringResource(id = R.string.following_count, account.followingCount),

            )
    }
}

@Composable
private fun UserBio(
    modifier: Modifier = Modifier,
    account: AccountUiState,
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
    account: AccountUiState,
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
        account.fields.forEachIndexed { index, field ->
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
            if (index < (account.fields.size) - 1) {
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
private fun Header(
    modifier: Modifier = Modifier,
    isUsersProfile: Boolean,
    accountUiState: AccountUiState,
    accountInteractions: AccountInteractions,
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                model = accountUiState.headerUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            Row {
                Spacer(modifier = Modifier.weight(1f))
                val buttonModifier = Modifier.padding(end = 8.dp)
                if (isUsersProfile) {
                    MoSoButton(
                        modifier = buttonModifier,
                        onClick = { /*TODO*/ }
                    ) {
                        Text(text = stringResource(id = R.string.edit_button))
                    }
                } else {
                    MoSoButton(
                        modifier = buttonModifier,
                        onClick = {
                            if (accountUiState.isFollowing) {
                                accountInteractions.onUnfollowClicked()
                            } else {
                                accountInteractions.onFollowClicked()
                            }
                        }
                    ) {
                        Text(
                            text = if (accountUiState.isFollowing) {
                                stringResource(id = R.string.unfollow_button)
                            } else {
                                stringResource(id = R.string.follow_button)
                            }
                        )
                    }
                }
            }

        }
        Column {
            Spacer(modifier = Modifier.padding(top = 60.dp))
            AsyncImage(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp)),
                model = accountUiState.avatarUrl,
                contentDescription = null,
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
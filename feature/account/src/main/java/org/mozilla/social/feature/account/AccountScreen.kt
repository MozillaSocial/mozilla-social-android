package org.mozilla.social.feature.account

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.paging.PagingData
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.toJavaLocalDateTime
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.common.Resource
import org.mozilla.social.common.utils.DateTimeFormatters
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.designsystem.component.MoSoButton
import org.mozilla.social.core.designsystem.component.MoSoButtonSecondary
import org.mozilla.social.core.designsystem.component.MoSoCircularProgressIndicator
import org.mozilla.social.core.designsystem.component.MoSoDivider
import org.mozilla.social.core.designsystem.component.MoSoDropdownMenu
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.component.MoSoTab
import org.mozilla.social.core.designsystem.component.MoSoTabRow
import org.mozilla.social.core.designsystem.component.MoSoToast
import org.mozilla.social.core.designsystem.component.MoSoTopBar
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.designsystem.utils.NoRipple
import org.mozilla.social.core.ui.DropDownItem
import org.mozilla.social.core.ui.error.GenericError
import org.mozilla.social.core.ui.htmlcontent.HtmlContent
import org.mozilla.social.core.ui.htmlcontent.HtmlContentInteractions
import org.mozilla.social.core.ui.postcard.PostCardInteractions
import org.mozilla.social.core.ui.postcard.PostCardList
import org.mozilla.social.core.ui.postcard.PostCardNavigation
import org.mozilla.social.core.ui.postcard.PostCardUiState
import kotlin.math.max

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
        timelineTypeFlow = viewModel.timelineType,
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
    timelineTypeFlow: StateFlow<TimelineType>,
    accountNavigationCallbacks: AccountNavigationCallbacks,
    htmlContentInteractions: HtmlContentInteractions,
    postCardInteractions: PostCardInteractions,
    accountInteractions: AccountInteractions,
) {
    MoSoSurface {
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
                        },
                        showDivider = false,
                    )

                    MainContent(
                        account = resource.data,
                        isUsersProfile = isUsersProfile,
                        feed = feed,
                        errorToastMessage = errorToastMessage,
                        htmlContentInteractions = htmlContentInteractions,
                        postCardInteractions = postCardInteractions,
                        accountInteractions = accountInteractions,
                        timelineTypeFlow = timelineTypeFlow,
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
}

@Composable
private fun MainContent(
    account: AccountUiState,
    isUsersProfile: Boolean,
    feed: Flow<PagingData<PostCardUiState>>,
    errorToastMessage: SharedFlow<StringFactory>,
    timelineTypeFlow: StateFlow<TimelineType>,
    htmlContentInteractions: HtmlContentInteractions,
    postCardInteractions: PostCardInteractions,
    accountInteractions: AccountInteractions,
) {
    val selectedTimelineType = timelineTypeFlow.collectAsState().value
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {

        PostCardList(
            feed = feed,
            errorToastMessage = errorToastMessage,
            refreshSignalFlow = timelineTypeFlow,
            postCardInteractions = postCardInteractions
        ) {
            Header(
                isUsersProfile = isUsersProfile,
                accountUiState = account,
                accountInteractions = accountInteractions,
            )

            UserInfo(account = account)
            Spacer(modifier = Modifier.height(12.dp))
            UserBio(
                account = account,
                htmlContentInteractions = htmlContentInteractions,
            )
            Spacer(modifier = Modifier.height(12.dp))
            //TODO add these back in when there are designs
//            UserFields(
//                account = account,
//                htmlContentInteractions = htmlContentInteractions,
//            )
            UserFollow(
                account = account,
                accountInteractions = accountInteractions,
            )
            MoSoTabRow(
                modifier = Modifier.padding(top = 20.dp),
                selectedTabIndex = selectedTimelineType.ordinal,
            ) {
                TimelineType.values().forEach { timelineType ->
                    MoSoTab(
                        modifier = Modifier
                            .height(40.dp),
                        selected = selectedTimelineType == timelineType,
                        onClick = { accountInteractions.onTabClicked(timelineType) },
                        content = {
                            Text(
                                text = timelineType.tabTitle.build(context),
                                style = MoSoTheme.typography.labelMedium
                            )
                        },
                    )
                }
            }
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
            style = MoSoTheme.typography.titleLarge
        )
        Text(
            text = "@${account.webFinger}",
            style = MoSoTheme.typography.labelMedium,
            color = MoSoTheme.colors.textSecondary,
        )
    }
}

@Composable
private fun UserFollow(
    account: AccountUiState,
    accountInteractions: AccountInteractions,
) {
    NoRipple {
        Row(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Counter(
                value = account.followersCount.toString(),
                label = stringResource(id = R.string.followers),
                onClick = { accountInteractions.onFollowersClicked() }
            )
            Spacer(modifier = Modifier.width(24.dp))
            Counter(
                value = account.followingCount.toString(),
                label = stringResource(id = R.string.following),
                onClick = { accountInteractions.onFollowingClicked() }
            )
            Spacer(modifier = Modifier.width(24.dp))
            Counter(
                value = account.statusesCount.toString(),
                label = stringResource(id = R.string.posts),
            )
        }
    }
}

@Composable
private fun Counter(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .clickable { onClick() },
    ) {
        Text(
            text = value,
            style = MoSoTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = label,
            style = MoSoTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun UserBio(
    modifier: Modifier = Modifier,
    account: AccountUiState,
    htmlContentInteractions: HtmlContentInteractions,
) {
    var expanded by remember { mutableStateOf(false) }
    NoRipple {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
                .clickable { expanded = !expanded },
        ) {
            val animationDuration = 150

            AnimatedContent(
                modifier = Modifier
                    .padding(end = 32.dp),
                targetState = expanded,
                label = "",
                transitionSpec = {
                    // expanding
                    if (targetState) {
                        EnterTransition.None togetherWith ExitTransition.None using
                                SizeTransform { _, _ ->
                                    keyframes {
                                        durationMillis = animationDuration
                                    }
                                }
                    } else { // shrinking
                        fadeIn(animationSpec = tween(0, 0)) togetherWith
                                fadeOut(animationSpec = tween(animationDuration)) using
                                SizeTransform { _, _ ->
                                    keyframes {
                                        durationMillis = animationDuration
                                    }
                                }
                    }

                }

            ) { targetState ->
                Column {
                    HtmlContent(
                        mentions = emptyList(),
                        htmlText = account.bio,
                        htmlContentInteractions = htmlContentInteractions,
                        textStyle = MoSoTheme.typography.bodyMedium,
                        maximumLineCount = if (targetState) Int.MAX_VALUE else 3,
                    )
                    if (targetState) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Icon(
                                modifier = Modifier
                                    .size(16.dp),
                                painter = MoSoIcons.userJoin(),
                                contentDescription = null,
                                tint = MoSoTheme.colors.textSecondary,
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(
                                    id = R.string.joined_date,
                                    DateTimeFormatters.standard.format(account.joinDate.toJavaLocalDateTime())
                                ),
                                style = MoSoTheme.typography.bodySmall,
                                color = MoSoTheme.colors.textSecondary,
                            )
                        }
                    }
                }
            }

            val rotation: Float by animateFloatAsState(
                targetValue = if (expanded) 180f else 0f,
                animationSpec = tween(animationDuration),
                label = ""
            )
            Icon(
                modifier = Modifier
                    .rotate(rotation)
                    .align(Alignment.TopEnd),
                imageVector = MoSoIcons.Caret,
                contentDescription = null,
            )
        }
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
private fun Header(
    modifier: Modifier = Modifier,
    isUsersProfile: Boolean,
    accountUiState: AccountUiState,
    accountInteractions: AccountInteractions,
) {
    HeaderLayout(
        modifier = modifier.fillMaxWidth(),
        headerImage = {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                model = accountUiState.headerUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        },
        profileImage = {
            AsyncImage(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(92.dp)
                    .clip(CircleShape)
                    .border(
                        width = 3.dp,
                        color = MoSoTheme.colors.layer1,
                        shape = CircleShape
                    ),
                model = accountUiState.avatarUrl,
                contentDescription = null,
            )
        },
        rightSideContent = {
            val buttonModifier = Modifier.padding(end = 8.dp)
            if (isUsersProfile) {
                MoSoButtonSecondary(
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
    )
}

/**
 * Layout for the header images and edit / follow buttons
 * Using a layout is nice because it automatically calculates the spacing for the
 * avatar image so you don't have to calculate the value yourself if the avatar size changes.
 */
@Composable
private fun HeaderLayout(
    modifier: Modifier = Modifier,
    headerImage: @Composable () -> Unit,
    profileImage: @Composable () -> Unit,
    rightSideContent: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = {
            Box { headerImage() }
            Box { profileImage() }
            Box { rightSideContent() }
        },
    ) { measurables, constraints ->
        val placeables = measurables.map {
            it.measure(constraints.copy(
                minWidth = 0,
                minHeight = 0,
            ))
        }
        val headerImagePlaceable = placeables[0]
        val profileImagePlaceable = placeables[1]
        val rightSideContentPlaceable = placeables[2]
        layout(
            width = constraints.maxWidth,
            height = headerImagePlaceable.height + max(profileImagePlaceable.height / 2, rightSideContentPlaceable.height),
        ) {
            headerImagePlaceable.placeRelative(
                x = 0,
                y = 0,
            )
            profileImagePlaceable.placeRelative(
                x = 0,
                y = headerImagePlaceable.height - profileImagePlaceable.height / 2
            )
            rightSideContentPlaceable.placeRelative(
                x = (constraints.maxWidth - rightSideContentPlaceable.width),
                y = headerImagePlaceable.height
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
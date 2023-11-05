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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.paging.PagingData
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
import org.mozilla.social.core.designsystem.component.MoSoDropdownMenu
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.component.MoSoTab
import org.mozilla.social.core.designsystem.component.MoSoTabRow
import org.mozilla.social.core.designsystem.component.MoSoToast
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.designsystem.utils.NoRipple
import org.mozilla.social.core.ui.common.DropDownItem
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar
import org.mozilla.social.core.ui.common.error.GenericError
import org.mozilla.social.core.ui.common.htmlcontent.HtmlContent
import org.mozilla.social.core.ui.common.htmlcontent.HtmlContentInteractions
import org.mozilla.social.core.ui.common.postcard.PostCardInteractions
import org.mozilla.social.core.ui.common.postcard.PostCardList
import org.mozilla.social.core.ui.common.postcard.PostCardUiState

@Composable
internal fun AccountScreen(
    windowInsets: WindowInsets = WindowInsets.systemBars,
    accountId: String?,
    viewModel: AccountViewModel = koinViewModel(parameters = { parametersOf(accountId) }),
) {
    AccountScreen(
        resource = viewModel.uiState.collectAsState().value,
        closeButtonVisible = viewModel.shouldShowCloseButton,
        isUsersProfile = viewModel.isOwnProfile,
        feed = viewModel.feed,
        errorToastMessage = viewModel.postCardDelegate.errorToastMessage,
        timelineTypeFlow = viewModel.timelineType,
        htmlContentInteractions = viewModel.postCardDelegate,
        postCardInteractions = viewModel.postCardDelegate,
        accountInteractions = viewModel,
        windowInsets = windowInsets,
    )

    LaunchedEffect(Unit) {
        viewModel.onAccountScreenShown()
    }

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
    htmlContentInteractions: HtmlContentInteractions,
    postCardInteractions: PostCardInteractions,
    accountInteractions: AccountInteractions,
    windowInsets: WindowInsets,
) {
    MoSoSurface {
        Column(
            modifier = Modifier
                .windowInsetsPadding(windowInsets)
        ) {
            when (resource) {
                is Resource.Loading -> {
                    MoSoCloseableTopAppBar(showCloseButton = closeButtonVisible)
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
                    MoSoCloseableTopAppBar(
                        title = resource.data.displayName,
                        showCloseButton = closeButtonVisible,
                        actions = {
                            OverflowMenu(
                                account = resource.data,
                                isUsersProfile = isUsersProfile,
                                overflowInteractions = accountInteractions,
                                navigateToSettings = accountInteractions::onSettingsClicked,
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
                    MoSoCloseableTopAppBar(showCloseButton = closeButtonVisible)
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
                headerUrl = account.headerUrl,
                avatarUrl = account.avatarUrl,
                displayName = account.displayName,
                handle = "@${account.webFinger}",
                rightSideContent = {
                    val buttonModifier = Modifier.padding(end = 8.dp)
                    if (isUsersProfile) {
                        MoSoButtonSecondary(
                            modifier = buttonModifier,
                            onClick = { accountInteractions.onEditAccountClicked() }
                        ) {
                            Text(text = stringResource(id = R.string.edit_button))
                        }
                    } else {
                        MoSoButton(
                            modifier = buttonModifier,
                            onClick = {
                                if (account.isFollowing) {
                                    accountInteractions.onUnfollowClicked()
                                } else {
                                    accountInteractions.onFollowClicked()
                                }
                            }
                        ) {
                            Text(
                                text = if (account.isFollowing) {
                                    stringResource(id = R.string.unfollow_button)
                                } else {
                                    stringResource(id = R.string.follow_button)
                                }
                            )
                        }
                    }
                },
            )

            Spacer(modifier = Modifier.height(12.dp))
            UserBio(
                account = account,
                htmlContentInteractions = htmlContentInteractions,
            )
            Spacer(modifier = Modifier.height(12.dp))
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
    navigateToSettings: () -> Unit,
) {
    val overflowMenuExpanded = remember { mutableStateOf(false) }
    val context = LocalContext.current
    IconButton(
        modifier = Modifier.width(IntrinsicSize.Max),
        onClick = { overflowMenuExpanded.value = true }
    ) {
        Icon(painter = MoSoIcons.moreVertical(), contentDescription = null)

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
            DropDownItem(
                text = stringResource(R.string.settings),
                expanded = overflowMenuExpanded,
                onClick = navigateToSettings,
            )

            if (!isUsersProfile) {
                if (account.isMuted) {
                    DropDownItem(
                        text = stringResource(
                            id = org.mozilla.social.core.ui.common.R.string.unmute_user,
                            account.username
                        ),
                        expanded = overflowMenuExpanded,
                        onClick = { overflowInteractions.onOverflowUnmuteClicked() }
                    )
                } else {
                    DropDownItem(
                        text = stringResource(
                            id = org.mozilla.social.core.ui.common.R.string.mute_user,
                            account.username
                        ),
                        expanded = overflowMenuExpanded,
                        onClick = { overflowInteractions.onOverflowMuteClicked() }
                    )
                }

                if (account.isBlocked) {
                    DropDownItem(
                        text = stringResource(
                            id = org.mozilla.social.core.ui.common.R.string.unblock_user,
                            account.username
                        ),
                        expanded = overflowMenuExpanded,
                        onClick = { overflowInteractions.onOverflowUnblockClicked() }
                    )
                } else {
                    DropDownItem(
                        text = stringResource(
                            id = org.mozilla.social.core.ui.common.R.string.block_user,
                            account.username
                        ),
                        expanded = overflowMenuExpanded,
                        onClick = { overflowInteractions.onOverflowBlockClicked() }
                    )
                }

                DropDownItem(
                    text = stringResource(
                        id = org.mozilla.social.core.ui.common.R.string.report_user,
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
                        maximumLineCount = if (targetState) Int.MAX_VALUE else BIO_MAX_LINES_NOT_EXPANDED,
                    )
                    if (targetState) {
                        Spacer(modifier = Modifier.height(8.dp))
                        account.fields.forEach { field ->
                            UserLabel(
                                label = field.name,
                                text = field.value,
                                htmlContentInteractions = htmlContentInteractions
                            )
                        }
                        UserLabel(
                            icon = MoSoIcons.userJoin(),
                            label = "",
                            text = stringResource(
                                id = R.string.joined_date,
                                DateTimeFormatters.standard.format(account.joinDate.toJavaLocalDateTime())
                            ),
                            htmlContentInteractions = htmlContentInteractions
                        )
                    }
                }
            }

            val rotatedDegrees = 180f

            val rotation: Float by animateFloatAsState(
                targetValue = if (expanded) rotatedDegrees else 0f,
                animationSpec = tween(animationDuration),
                label = ""
            )
            Icon(
                modifier = Modifier
                    .rotate(rotation)
                    .align(Alignment.TopEnd),
                painter = MoSoIcons.caret(),
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun UserLabel(
    icon: Painter? = null,
    label: String? = null,
    text: String,
    htmlContentInteractions: HtmlContentInteractions,
) {
    Row {
        icon?.let {
            Icon(
                modifier = Modifier
                    .size(16.dp),
                painter = icon,
                contentDescription = null,
                tint = MoSoTheme.colors.textSecondary,
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        if (!label.isNullOrBlank()) {
            Text(
                text = label,
                style = MoSoTheme.typography.bodySmall,
                color = MoSoTheme.colors.textSecondary,
                fontWeight = W700,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        HtmlContent(
            modifier = Modifier
                .weight(1f),
            mentions = emptyList(),
            htmlText = text,
            htmlContentInteractions = htmlContentInteractions,
            textStyle = MoSoTheme.typography.bodySmall,
            textColor = MoSoTheme.colors.textSecondary,
            linkColor = MoSoTheme.colors.textSecondary,
            maximumLineCount = 1,
        )
    }
}

private const val BIO_MAX_LINES_NOT_EXPANDED = 3

@Preview
@Composable
fun AccountScreenPreview() {
    MoSoTheme {
//        AccountScreen("110810174933375392")
    }
}
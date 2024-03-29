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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalDateTime
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.common.Resource
import org.mozilla.social.common.utils.DateTimeFormatters
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.designsystem.utils.NoRipple
import org.mozilla.social.core.model.AccountTimelineType
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.tabs.MoSoTab
import org.mozilla.social.core.ui.common.tabs.MoSoTabRow
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar
import org.mozilla.social.core.ui.common.button.MoSoButton
import org.mozilla.social.core.ui.common.button.MoSoButtonContentPadding
import org.mozilla.social.core.ui.common.button.MoSoButtonSecondary
import org.mozilla.social.core.ui.common.dropdown.MoSoDropDownItem
import org.mozilla.social.core.ui.common.dropdown.MoSoDropdownMenu
import org.mozilla.social.core.ui.common.error.GenericError
import org.mozilla.social.core.ui.common.loading.MoSoCircularProgressIndicator
import org.mozilla.social.core.ui.common.paging.PagingLazyColumn
import org.mozilla.social.core.ui.common.utils.PreviewTheme
import org.mozilla.social.core.ui.common.text.SmallTextLabel
import org.mozilla.social.core.ui.htmlcontent.HtmlContent
import org.mozilla.social.core.ui.htmlcontent.HtmlContentInteractions
import org.mozilla.social.core.ui.postcard.PostCardInteractions
import org.mozilla.social.core.ui.postcard.postListContent

@Composable
internal fun AccountScreen(
    windowInsets: WindowInsets = WindowInsets.systemBars,
    accountId: String?,
    viewModel: AccountViewModel = koinViewModel(parameters = { parametersOf(accountId) }),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val timeline by viewModel.timeline.collectAsStateWithLifecycle()
    AccountScreen(
        uiState = uiState,
        closeButtonVisible = viewModel.shouldShowCloseButton,
        isUsersProfile = viewModel.isOwnProfile,
        timeline = timeline,
        htmlContentInteractions = viewModel.postCardDelegate,
        postCardInteractions = viewModel.postCardDelegate,
        accountInteractions = viewModel,
        windowInsets = windowInsets,
        navigateToSettings = viewModel::onSettingsClicked,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@Composable
private fun AccountScreen(
    navigateToSettings: () -> Unit,
    uiState: Resource<AccountUiState>,
    closeButtonVisible: Boolean,
    isUsersProfile: Boolean,
    timeline: Timeline,
    htmlContentInteractions: HtmlContentInteractions,
    postCardInteractions: PostCardInteractions,
    accountInteractions: AccountInteractions,
    windowInsets: WindowInsets,
) {
    MoSoSurface {
        Column(
            modifier =
                Modifier
                    .windowInsetsPadding(windowInsets),
        ) {
            when (uiState) {
                is Resource.Loading -> {
                    MoSoCloseableTopAppBar(showCloseButton = closeButtonVisible)
                    Box(
                        modifier =
                        Modifier
                            .fillMaxSize()
                            .wrapContentSize(
                                align = Alignment.Center,
                            ),
                    ) {
                        MoSoCircularProgressIndicator()
                    }
                }

                is Resource.Loaded<AccountUiState> -> {
                    MoSoCloseableTopAppBar(
                        title = uiState.data.displayName,
                        showCloseButton = closeButtonVisible,
                        actions = {
                            if (isUsersProfile) {
                                IconButton(
                                    modifier = Modifier.width(IntrinsicSize.Max),
                                    onClick = navigateToSettings,
                                ) {
                                    Icon(
                                        painter = MoSoIcons.gear(),
                                        contentDescription = "Settings"
                                    )
                                }
                            }
                        },
                        showDivider = false,
                    )

                    MainContent(
                        account = uiState.data,
                        isUsersProfile = isUsersProfile,
                        htmlContentInteractions = htmlContentInteractions,
                        postCardInteractions = postCardInteractions,
                        accountInteractions = accountInteractions,
                        timeline = timeline,
                    )
                }

                is Resource.Error -> {
                    MoSoCloseableTopAppBar(showCloseButton = closeButtonVisible)
                    Box(
                        modifier =
                        Modifier
                            .fillMaxSize()
                            .wrapContentSize(
                                align = Alignment.Center,
                            ),
                    ) {
                        GenericError(
                            onRetryClicked = {
                                accountInteractions.onRetryClicked()
                            },
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
    timeline: Timeline,
    htmlContentInteractions: HtmlContentInteractions,
    postCardInteractions: PostCardInteractions,
    accountInteractions: AccountInteractions,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize(),
    ) {
        val postsFeed = timeline.postsFeed.collectAsLazyPagingItems()
        val postsAndRepliesFeed = timeline.postsAndRepliesFeed.collectAsLazyPagingItems()
        val mediaFeed = timeline.mediaFeed.collectAsLazyPagingItems()

        val currentFeed = when (timeline.type) {
            AccountTimelineType.POSTS -> postsFeed
            AccountTimelineType.POSTS_AND_REPLIES -> postsAndRepliesFeed
            AccountTimelineType.MEDIA -> mediaFeed
        }

        val listState = rememberLazyListState()

        PagingLazyColumn(
            lazyPagingItems = currentFeed,
            headerContent = {
                item {
                    MainAccount(
                        account = account,
                        isUsersProfile = isUsersProfile,
                        timeline = timeline,
                        htmlContentInteractions = htmlContentInteractions,
                        accountInteractions = accountInteractions,
                    )
                }
            },
            listState = listState,
            emptyListState = listState,
        ) {
            postListContent(
                lazyPagingItems = currentFeed,
                postCardInteractions = postCardInteractions,
            )
        }
    }
}

@Composable
private fun MainAccount(
    account: AccountUiState,
    isUsersProfile: Boolean,
    timeline: Timeline,
    htmlContentInteractions: HtmlContentInteractions,
    accountInteractions: AccountInteractions,
) {
    Header(
        headerUrl = account.headerUrl,
        avatarUrl = account.avatarUrl,
        displayName = account.displayName,
        handle = "@${account.webFinger}",
        rightSideContent = {
            val buttonModifier = Modifier
                .padding(end = 8.dp)
            Row {
                if (isUsersProfile) {
                    MoSoButtonSecondary(
                        modifier = buttonModifier
                            .align(Alignment.CenterVertically),
                        onClick = { accountInteractions.onEditAccountClicked() },
                        contentPadding = MoSoButtonContentPadding.small,
                    ) {
                        SmallTextLabel(text = stringResource(id = R.string.edit_button))
                    }
                } else {
                    MoSoButton(
                        modifier = buttonModifier
                            .align(Alignment.CenterVertically),
                        onClick = {
                            if (account.isFollowing) {
                                accountInteractions.onUnfollowClicked()
                            } else {
                                accountInteractions.onFollowClicked()
                            }
                        },
                        contentPadding = MoSoButtonContentPadding.small,
                    ) {
                        SmallTextLabel(
                            text =
                            if (account.isFollowing) {
                                stringResource(id = R.string.unfollow_button)
                            } else {
                                stringResource(id = R.string.follow_button)
                            },
                        )
                    }
                }

                OverflowMenu(
                    account = account,
                    isUsersProfile = isUsersProfile,
                    overflowInteractions = accountInteractions,
                )
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
        selectedTabIndex = timeline.type.ordinal,
    ) {
        AccountTimelineType.entries.forEach { timelineType ->
            MoSoTab(
                modifier =
                Modifier,
                selected = timeline.type == timelineType,
                onClick = { accountInteractions.onTabClicked(timelineType) },
                content = {
                    Text(
                        text = when (timelineType) {
                            AccountTimelineType.POSTS -> stringResource(id = R.string.tab_posts)
                            AccountTimelineType.POSTS_AND_REPLIES ->
                                stringResource(id = R.string.tab_posts_and_replies)
                            AccountTimelineType.MEDIA -> stringResource(id = R.string.tab_media)
                        },
                        style = MoSoTheme.typography.labelMedium,
                    )
                },
            )
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
        onClick = { overflowMenuExpanded.value = true },
    ) {
        Icon(
            painter = MoSoIcons.moreVertical(),
            contentDescription = stringResource(R.string.overflow_button)
        )

        MoSoDropdownMenu(
            expanded = overflowMenuExpanded.value,
            onDismissRequest = {
                overflowMenuExpanded.value = false
            },
        ) {
            if (isUsersProfile) {
                MoSoDropDownItem(
                    text = stringResource(id = R.string.favorites_option),
                    expanded = overflowMenuExpanded,
                    onClick = { overflowInteractions.onOverflowFavoritesClicked() }
                )
            }

            MoSoDropDownItem(
                text = stringResource(R.string.share_option),
                expanded = overflowMenuExpanded,
                onClick = {
                    overflowInteractions.onOverflowShareClicked()
                    val sendIntent: Intent =
                        Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, account.accountUrl)
                            type = "text/plain"
                        }

                    ContextCompat.startActivity(
                        context,
                        Intent.createChooser(sendIntent, null),
                        null,
                    )
                },
            )

            if (!isUsersProfile) {
                if (account.isMuted) {
                    MoSoDropDownItem(
                        text =
                            stringResource(
                                id = org.mozilla.social.core.ui.common.R.string.unmute_user,
                                account.username,
                            ),
                        expanded = overflowMenuExpanded,
                        onClick = { overflowInteractions.onOverflowUnmuteClicked() },
                    )
                } else {
                    MoSoDropDownItem(
                        text =
                            stringResource(
                                id = org.mozilla.social.core.ui.common.R.string.mute_user,
                                account.username,
                            ),
                        expanded = overflowMenuExpanded,
                        onClick = { overflowInteractions.onOverflowMuteClicked() },
                    )
                }

                if (account.isBlocked) {
                    MoSoDropDownItem(
                        text =
                            stringResource(
                                id = org.mozilla.social.core.ui.common.R.string.unblock_user,
                                account.username,
                            ),
                        expanded = overflowMenuExpanded,
                        onClick = { overflowInteractions.onOverflowUnblockClicked() },
                    )
                } else {
                    MoSoDropDownItem(
                        text =
                            stringResource(
                                id = org.mozilla.social.core.ui.common.R.string.block_user,
                                account.username,
                            ),
                        expanded = overflowMenuExpanded,
                        onClick = { overflowInteractions.onOverflowBlockClicked() },
                    )
                }

                MoSoDropDownItem(
                    text =
                        stringResource(
                            id = org.mozilla.social.core.ui.common.R.string.report_user,
                            account.username,
                        ),
                    expanded = overflowMenuExpanded,
                    onClick = { overflowInteractions.onOverflowReportClicked() },
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
            modifier =
                Modifier
                    .padding(8.dp),
        ) {
            Counter(
                value = account.followersCount.toString(),
                label = stringResource(id = R.string.followers),
                onClick = { accountInteractions.onFollowersClicked() },
            )
            Spacer(modifier = Modifier.width(24.dp))
            Counter(
                value = account.followingCount.toString(),
                label = stringResource(id = R.string.following),
                onClick = { accountInteractions.onFollowingClicked() },
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
        modifier =
            modifier
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
    val bioIsBlank by remember(account.bio) { mutableStateOf(account.bio.isBlank()) }

    NoRipple {
        Box(
            modifier =
            modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
                .clickable { expanded = !expanded },
        ) {
            val animationDuration = 150

            AnimatedContent(
                modifier =
                    Modifier
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
                },
            ) { targetState ->
                Column {
                    if (!bioIsBlank) {
                        HtmlContent(
                            mentions = emptyList(),
                            htmlText = account.bio,
                            htmlContentInteractions = htmlContentInteractions,
                            maximumLineCount = if (targetState) Int.MAX_VALUE else BIO_MAX_LINES_NOT_EXPANDED,
                        )
                    }
                    if (targetState || bioIsBlank) {
                        if (!bioIsBlank) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        account.fields.forEach { field ->
                            UserLabel(
                                label = field.name,
                                text = field.value,
                                htmlContentInteractions = htmlContentInteractions,
                            )
                        }
                        UserLabel(
                            icon = MoSoIcons.userJoin(),
                            label = "",
                            text =
                                stringResource(
                                    id = R.string.joined_date,
                                    DateTimeFormatters.standard.format(account.joinDate.toJavaLocalDateTime()),
                                ),
                            htmlContentInteractions = htmlContentInteractions,
                        )
                    }
                }
            }

            if (!bioIsBlank) {
                val rotatedDegrees = 180f

                val rotation: Float by animateFloatAsState(
                    targetValue = if (expanded) rotatedDegrees else 0f,
                    animationSpec = tween(animationDuration),
                    label = "",
                )
                Icon(
                    modifier =
                    Modifier
                        .rotate(rotation)
                        .align(Alignment.TopEnd),
                    painter = MoSoIcons.caret(),
                    contentDescription = null,
                )
            }
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
                modifier =
                    Modifier
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
            modifier =
                Modifier
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

@Suppress("MagicNumber")
@Preview
@Composable
fun AccountScreenPreview() {
    PreviewTheme(modules = listOf(navigationModule)) {
        AccountScreen(
            uiState =
            Resource.Loaded(
                data =
                AccountUiState(
                    accountId = "",
                    username = "Coolguy",
                    webFinger = "coolguy",
                    displayName = "Cool Guy",
                    accountUrl = "",
                    bio = "I'm pretty cool",
                    avatarUrl = "",
                    headerUrl = "",
                    followersCount = 1,
                    followingCount = 500,
                    statusesCount = 4000,
                    fields = listOf(),
                    isBot = false,
                    isFollowing = false,
                    isMuted = false,
                    isBlocked = false,
                    joinDate =
                    LocalDateTime(
                        LocalDate(2023, 7, 3),
                        LocalTime(0, 0, 0),
                    ),
                ),
            ),
            closeButtonVisible = true,
            isUsersProfile = false,
            timeline = Timeline(
                type = AccountTimelineType.POSTS,
                postsFeed = flowOf(),
                postsAndRepliesFeed = flowOf(),
                mediaFeed = flowOf(),
            ),
            htmlContentInteractions = object : HtmlContentInteractions {},
            postCardInteractions = object : PostCardInteractions {},
            accountInteractions = object : AccountInteractions {},
            windowInsets = WindowInsets.systemBars,
            navigateToSettings = {},
        )
    }
}

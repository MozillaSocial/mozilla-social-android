package org.mozilla.social.search

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.common.Resource
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoRadius
import org.mozilla.social.core.designsystem.theme.MoSoSpacing
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.designsystem.utils.NoRipple
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.ui.accountfollower.AccountFollower
import org.mozilla.social.core.ui.accountfollower.AccountFollowerUiState
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.tabs.MoSoTab
import org.mozilla.social.core.ui.common.tabs.MoSoTabRow
import org.mozilla.social.core.ui.common.account.quickview.AccountQuickViewBox
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar
import org.mozilla.social.core.ui.common.divider.MoSoDivider
import org.mozilla.social.core.ui.common.error.GenericError
import org.mozilla.social.core.ui.common.following.FollowingButton
import org.mozilla.social.core.ui.common.hashtag.quickview.HashTagQuickView
import org.mozilla.social.core.ui.common.hashtag.quickview.HashTagQuickViewUiState
import org.mozilla.social.core.ui.common.loading.MaxSizeLoading
import org.mozilla.social.core.ui.common.paging.PagingLazyColumn
import org.mozilla.social.core.ui.common.search.MoSoSearchBar
import org.mozilla.social.core.ui.common.utils.PreviewTheme
import org.mozilla.social.core.ui.postcard.PostCard
import org.mozilla.social.core.ui.postcard.PostCardInteractions
import org.mozilla.social.core.ui.postcard.PostCardUiState
import org.mozilla.social.core.ui.postcard.postListContent
import org.mozilla.social.feature.search.R

@Composable
internal fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SearchScreen(
        uiState = uiState,
        searchInteractions = viewModel,
        postCardInteractions = viewModel.postCardDelegate,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchScreen(
    uiState: SearchUiState,
    searchInteractions: SearchInteractions,
    postCardInteractions: PostCardInteractions,
) {
    MoSoSurface {
        Column(Modifier.systemBarsPadding()) {
            val searchFocusRequester = remember { FocusRequester() }
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current
            var searchHasFocus by remember {
                mutableStateOf(uiState.query.isBlank())
            }

            LaunchedEffect(Unit) {
                if (uiState.query.isBlank()) {
                    searchFocusRequester.requestFocus()
                }
            }

            MoSoCloseableTopAppBar(
                actions = {
                    MoSoSearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                searchHasFocus = it.hasFocus
                            }
                            .focusRequester(searchFocusRequester)
                            .padding(
                                end = MoSoSpacing.md,
                                start = 60.dp,
                            ),
                        query = uiState.query,
                        onQueryChange = { searchInteractions.onQueryTextChanged(it) },
                        onSearch = {
                            if (uiState.query.isNotBlank()) {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                                searchInteractions.onSearchClicked()
                            }
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    searchInteractions.onQueryTextChanged("")
                                    searchFocusRequester.requestFocus()
                                }
                            ) {
                                Icon(
                                    modifier = Modifier.size(16.dp),
                                    painter = MoSoIcons.x(),
                                    contentDescription = stringResource(id = R.string.clear_search),
                                    tint = MoSoTheme.colors.iconSecondary,
                                )
                            }
                        },
                    )
                }
            )

            Box {
                Column {
                    Tabs(
                        uiState = uiState,
                        searchInteractions = searchInteractions,
                    )
                    ListContent(
                        uiState = uiState,
                        searchInteractions = searchInteractions,
                        postCardInteractions = postCardInteractions,
                    )
                }
                androidx.compose.animation.AnimatedVisibility(
                    modifier = Modifier.background(MoSoTheme.colors.layer1),
                    visible = searchHasFocus,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Suggestions()
                }
            }
        }
    }
}

@Composable
private fun Tabs(
    uiState: SearchUiState,
    searchInteractions: SearchInteractions,
) {
    val context = LocalContext.current

    MoSoTabRow(selectedTabIndex = uiState.selectedTab.ordinal) {
        SearchTab.entries.forEach { tabType ->
            MoSoTab(
                modifier =
                Modifier
                    .height(40.dp),
                selected = uiState.selectedTab == tabType,
                onClick = { searchInteractions.onTabClicked(tabType) },
                content = {
                    Text(
                        text = tabType.tabTitle.build(context),
                        style = MoSoTheme.typography.labelMedium,
                    )
                },
            )
        }
    }
}

@Composable
private fun ListContent(
    uiState: SearchUiState,
    searchInteractions: SearchInteractions,
    postCardInteractions: PostCardInteractions,
) {
    when (uiState.topResource) {
        is Resource.Loading -> {
            MaxSizeLoading()
        }
        is Resource.Error -> {
            GenericError(
                onRetryClicked = { searchInteractions.onRetryClicked() }
            )
        }
        is Resource.Loaded -> {
            val accountFeed = uiState.accountsFeed?.collectAsLazyPagingItems()
            val hashTagFeed = uiState.hashTagFeed?.collectAsLazyPagingItems()
            val statusFeed = uiState.statusFeed?.collectAsLazyPagingItems()

            val topScrollState = rememberLazyListState()
            val topAccountsScrollState = rememberLazyListState()
            val accountScrollState = rememberLazyListState()
            val hashTagScrollState = rememberLazyListState()
            val statusScrollState = rememberLazyListState()

            when (uiState.selectedTab) {
                SearchTab.TOP -> {
                    TopList(
                        searchResultUiState = uiState.topResource.data,
                        scrollState = topScrollState,
                        topAccountsScrollState = topAccountsScrollState,
                        searchInteractions = searchInteractions,
                        postCardInteractions = postCardInteractions,
                    )
                }
                SearchTab.ACCOUNTS -> {
                    AccountsList(
                        accountFeed = accountFeed,
                        scrollState = accountScrollState,
                        searchInteractions = searchInteractions,
                    )
                }
                SearchTab.HASHTAGS -> {
                    HashTagsList(
                        hashTagsFeed = hashTagFeed,
                        scrollState = hashTagScrollState,
                        searchInteractions = searchInteractions,
                    )
                }
                SearchTab.POSTS -> {
                    StatusesList(
                        statusFeed = statusFeed,
                        scrollState = statusScrollState,
                        postCardInteractions = postCardInteractions,
                    )
                }
            }
        }
    }
}

@Composable
private fun TopList(
    searchResultUiState: SearchResultUiState,
    scrollState: LazyListState,
    topAccountsScrollState: LazyListState,
    searchInteractions: SearchInteractions,
    postCardInteractions: PostCardInteractions,
) {
    LazyColumn(
        Modifier
            .fillMaxSize(),
        state = scrollState,
    ) {
        if (searchResultUiState.accountUiStates.isNotEmpty()) {
            item {
                TopAccounts(
                    searchResultUiState = searchResultUiState,
                    scrollState = topAccountsScrollState,
                    searchInteractions = searchInteractions,
                )
            }
        }

        if (searchResultUiState.postCardUiStates.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .padding(MoSoSpacing.md)
                        .clickable { searchInteractions.onTabClicked(SearchTab.POSTS) }
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = stringResource(id = R.string.posts_tab),
                        style = MoSoTheme.typography.titleMedium,
                    )
                    Icon(painter = MoSoIcons.caretRight(), contentDescription = "")
                }
            }

            items(
                count = searchResultUiState.postCardUiStates.count(),
                key = { searchResultUiState.postCardUiStates[it].statusId },
            ) { index ->
                val item = searchResultUiState.postCardUiStates[index]
                PostCard(
                    post = item,
                    postCardInteractions = postCardInteractions,
                )
                if (index < searchResultUiState.postCardUiStates.count()) {
                    MoSoDivider()
                }
            }
        }

        if (searchResultUiState.accountUiStates.isEmpty() &&
            searchResultUiState.postCardUiStates.isEmpty()) {
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding(16.dp),
                    text = stringResource(id = R.string.search_empty),
                )
            }
        }
    }
}

@Composable
private fun TopAccounts(
    searchResultUiState: SearchResultUiState,
    scrollState: LazyListState,
    searchInteractions: SearchInteractions,
) {
    Row(
        modifier = Modifier
            .padding(start = MoSoSpacing.md, end = MoSoSpacing.md, top = MoSoSpacing.md)
            .clickable { searchInteractions.onTabClicked(SearchTab.ACCOUNTS) }
    ) {
        Text(
            modifier = Modifier
                .weight(1f),
            text = stringResource(id = R.string.accounts_tab),
            style = MoSoTheme.typography.titleMedium,
        )
        Icon(painter = MoSoIcons.caretRight(), contentDescription = "")
    }
    LazyRow(
        state = scrollState,
    ) {
        items(
            count = searchResultUiState.accountUiStates.count(),
            key = { searchResultUiState.accountUiStates[it].quickViewUiState.accountId },
        ) { index ->
            val item = searchResultUiState.accountUiStates[index]
            NoRipple {
                AccountQuickViewBox(
                    modifier = Modifier
                        .padding(MoSoSpacing.md)
                        .border(
                            width = 1.dp,
                            color = MoSoTheme.colors.borderPrimary,
                            shape = RoundedCornerShape(MoSoRadius.lg_16_dp)
                        )
                        .clickable {
                            searchInteractions.onAccountClicked(item.quickViewUiState.accountId)
                        }
                        .width(256.dp)
                        .padding(MoSoSpacing.md),
                    uiState = item.quickViewUiState,
                    buttonSlot = {
                        if (item.followButtonVisible) {
                            FollowingButton(
                                onButtonClicked = {
                                    searchInteractions.onFollowClicked(
                                        item.quickViewUiState.accountId,
                                        item.isFollowing
                                    )
                                },
                                isFollowing = item.isFollowing
                            )
                        }
                    },
                )
            }
        }
    }

    MoSoDivider()
}

@Composable
private fun AccountsList(
    accountFeed: LazyPagingItems<AccountFollowerUiState>?,
    scrollState: LazyListState,
    searchInteractions: SearchInteractions,
) {
    accountFeed?.let { lazyPagingItems ->
        PagingLazyColumn(
            lazyPagingItems = lazyPagingItems,
            noResultText = stringResource(id = R.string.search_empty),
            listState = scrollState,
        ) {
            items(
                count = lazyPagingItems.itemCount,
                key = lazyPagingItems.itemKey { it.accountQuickViewUiState.accountId },
            ) { index ->
                lazyPagingItems[index]?.let { uiState ->
                    AccountFollower(
                        uiState = uiState,
                        onButtonClicked = {
                            searchInteractions.onFollowClicked(
                                uiState.accountQuickViewUiState.accountId,
                                isFollowing = uiState.isFollowing,
                            )
                        },
                        modifier = Modifier
                            .padding(MoSoSpacing.md)
                            .clickable {
                                searchInteractions.onAccountClicked(
                                    accountId = uiState.accountQuickViewUiState.accountId
                                )
                            },
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusesList(
    statusFeed: LazyPagingItems<PostCardUiState>?,
    scrollState: LazyListState,
    postCardInteractions: PostCardInteractions,
) {
    statusFeed?.let { lazyPagingItems ->
        PagingLazyColumn(
            lazyPagingItems = lazyPagingItems,
            noResultText = stringResource(id = R.string.search_empty),
            listState = scrollState,
        ) {
            postListContent(
                lazyPagingItems = lazyPagingItems,
                postCardInteractions = postCardInteractions,
            )
        }
    }
}

@Composable
private fun HashTagsList(
    hashTagsFeed: LazyPagingItems<HashTagQuickViewUiState>?,
    scrollState: LazyListState,
    searchInteractions: SearchInteractions,
) {
    hashTagsFeed?.let { lazyPagingItems ->
        PagingLazyColumn(
            lazyPagingItems = lazyPagingItems,
            noResultText = stringResource(id = R.string.search_empty),
            listState = scrollState,
        ) {
            items(
                count = lazyPagingItems.itemCount,
                key = lazyPagingItems.itemKey { it.name },
            ) { index ->
                lazyPagingItems[index]?.let { uiState ->
                    HashTagQuickView(
                        modifier = Modifier
                            .padding(MoSoSpacing.md)
                            .clickable { searchInteractions.onHashTagClicked(uiState.name) },
                        uiState = uiState,
                        onButtonClicked = {
                            searchInteractions.onHashTagFollowClicked(
                                name = uiState.name,
                                isFollowing = uiState.isFollowing
                            )
                        }
                    )
                }
            }
        }
    }
}

//TODO add search suggestions or recent searches
@Composable
private fun Suggestions() {
    Box(
        modifier = Modifier.fillMaxSize()
    )
}

@Preview
@Composable
private fun SearchScreenPreview() {
    PreviewTheme(
        modules = listOf(navigationModule)
    ) {
        SearchScreen(
            uiState = SearchUiState(
                topResource = Resource.Loaded(
                    SearchResultUiState(
                        postCardUiStates = listOf(),
                        accountUiStates = listOf(),
                    )
                ),
                query = "test",
            ),
            searchInteractions = object : SearchInteractions {},
            postCardInteractions = object : PostCardInteractions {},
        )
    }
}

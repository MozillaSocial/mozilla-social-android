package social.firefly.search

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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import social.firefly.common.Resource
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfRadius
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.designsystem.utils.NoRipple
import social.firefly.core.navigation.navigationModule
import social.firefly.core.ui.accountfollower.AccountFollower
import social.firefly.core.ui.accountfollower.AccountFollowerUiState
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.account.quickview.AccountQuickViewBox
import social.firefly.core.ui.common.appbar.FfCloseableTopAppBar
import social.firefly.core.ui.common.divider.FfDivider
import social.firefly.core.ui.common.error.GenericError
import social.firefly.core.ui.common.following.FollowingButton
import social.firefly.core.ui.common.hashtag.quickview.HashTagQuickView
import social.firefly.core.ui.common.hashtag.quickview.HashTagQuickViewUiState
import social.firefly.core.ui.common.loading.MaxSizeLoading
import social.firefly.core.ui.common.paging.PagingLazyColumn
import social.firefly.core.ui.common.search.FfSearchBar
import social.firefly.core.ui.common.tabs.FfTab
import social.firefly.core.ui.common.tabs.FfTabRow
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.core.ui.postcard.PostCard
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.PostCardUiState
import social.firefly.core.ui.postcard.postListContent
import social.firefly.feature.search.R

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

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreen(
    uiState: SearchUiState,
    searchInteractions: SearchInteractions,
    postCardInteractions: PostCardInteractions,
) {
    FfSurface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column {
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

            Box {
                FfCloseableTopAppBar()
                FfSearchBar(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxWidth()
                        .onFocusChanged {
                            searchHasFocus = it.hasFocus
                        }
                        .focusRequester(searchFocusRequester)
                        .padding(
                            end = FfSpacing.md,
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
                                modifier = Modifier.size(FfIcons.Sizes.small),
                                painter = FfIcons.x(),
                                contentDescription = stringResource(id = R.string.clear_search),
                                tint = FfTheme.colors.iconSecondary,
                            )
                        }
                    },
                )
            }


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
                    modifier = Modifier.background(FfTheme.colors.layer1),
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

    FfTabRow(selectedTabIndex = uiState.selectedTab.ordinal) {
        SearchTab.entries.forEach { tabType ->
            FfTab(
                modifier =
                Modifier
                    .height(40.dp),
                selected = uiState.selectedTab == tabType,
                onClick = { searchInteractions.onTabClicked(tabType) },
                content = {
                    Text(
                        text = tabType.tabTitle.build(context),
                        style = FfTheme.typography.labelMedium,
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
                        .padding(FfSpacing.md)
                        .clickable { searchInteractions.onTabClicked(SearchTab.POSTS) }
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = stringResource(id = R.string.posts_tab),
                        style = FfTheme.typography.titleMedium,
                    )
                    Icon(painter = FfIcons.caretRight(), contentDescription = "")
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
                    FfDivider()
                }
            }
        }

        if (searchResultUiState.accountUiStates.isEmpty() &&
            searchResultUiState.postCardUiStates.isEmpty()
        ) {
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
            .padding(start = FfSpacing.md, end = FfSpacing.md, top = FfSpacing.md)
            .clickable { searchInteractions.onTabClicked(SearchTab.ACCOUNTS) }
    ) {
        Text(
            modifier = Modifier
                .weight(1f),
            text = stringResource(id = R.string.accounts_tab),
            style = FfTheme.typography.titleMedium,
        )
        Icon(painter = FfIcons.caretRight(), contentDescription = "")
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
                        .padding(FfSpacing.md)
                        .border(
                            width = 1.dp,
                            color = FfTheme.colors.borderPrimary,
                            shape = RoundedCornerShape(FfRadius.lg_16_dp)
                        )
                        .clickable {
                            searchInteractions.onAccountClicked(item.quickViewUiState.accountId)
                        }
                        .width(256.dp)
                        .padding(FfSpacing.md),
                    uiState = item.quickViewUiState,
                    buttonSlot = {
                        if (item.followButtonVisible) {
                            FollowingButton(
                                onButtonClicked = {
                                    searchInteractions.onFollowClicked(
                                        item.quickViewUiState.accountId,
                                        item.followStatus
                                    )
                                },
                                followStatus = item.followStatus
                            )
                        }
                    },
                )
            }
        }
    }

    FfDivider()
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
                                followStatus = uiState.followStatus,
                            )
                        },
                        modifier = Modifier
                            .padding(FfSpacing.md)
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
                            .padding(FfSpacing.md)
                            .clickable { searchInteractions.onHashTagClicked(uiState.name) },
                        uiState = uiState,
                        onButtonClicked = {
                            searchInteractions.onHashTagFollowClicked(
                                name = uiState.name,
                                followStatus = uiState.followStatus
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

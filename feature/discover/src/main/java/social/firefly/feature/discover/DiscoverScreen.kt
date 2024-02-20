package social.firefly.feature.discover

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import social.firefly.common.Resource
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfRadius
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.designsystem.utils.NoRipple
import social.firefly.core.model.Recommendation
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.appbar.FfTopBar
import social.firefly.core.ui.common.divider.FfDivider
import social.firefly.core.ui.common.error.GenericError
import social.firefly.core.ui.common.loading.MaxSizeLoading
import social.firefly.core.ui.common.search.FfSearchBar
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.core.ui.common.utils.media
import social.firefly.core.ui.common.utils.shareUrl

@Composable
internal fun DiscoverScreen(viewModel: DiscoverViewModel = koinViewModel()) {
    val recommendations by viewModel.recommendations.collectAsStateWithLifecycle()
    DiscoverScreen(
        recommendations = recommendations,
        discoverInteractions = viewModel,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscoverScreen(
    recommendations: Resource<List<Recommendation>>,
    discoverInteractions: DiscoverInteractions,
) {
    val searchField = stringResource(id = R.string.search_field)
    FfSurface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
            ) {
                // Adding an empty top bar ensure the search bar will align with the
                // search bar on the search screen
                FfTopBar(title = { })
                FfSearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterEnd)
                        .padding(
                            end = FfSpacing.md,
                            start = FfSpacing.md,
                        ),
                    query = "",
                    onQueryChange = {},
                    onSearch = {},
                    readOnly = true,
                )
                NoRipple {
                    // invisible box that intercepts clicks
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { discoverInteractions.onSearchBarClicked() }
                            .semantics {
                                contentDescription = searchField
                            }
                    )
                }
            }
            when (recommendations) {
                is Resource.Loaded -> {
                    MainContent(
                        recommendations = recommendations.data,
                        discoverInteractions = discoverInteractions,
                    )
                }

                is Resource.Loading -> {
                    MaxSizeLoading()
                }

                is Resource.Error -> {
                    GenericError(
                        onRetryClicked = {
                            discoverInteractions.onRetryClicked()
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun MainContent(
    recommendations: List<Recommendation>,
    discoverInteractions: DiscoverInteractions,
) {
    LazyColumn {
        item {
            Text(
                modifier =
                Modifier
                    .padding(start = 16.dp, top = 8.dp),
                text = stringResource(id = R.string.discover_title),
                style = FfTheme.typography.titleLarge,
            )
        }
        items(
            count = recommendations.size,
            key = { recommendations[it].id },
        ) { index ->
            Recommendation(
                recommendation = recommendations[index],
                discoverInteractions = discoverInteractions,
            )
            if (index < recommendations.size - 1) {
                FfDivider()
            }
        }
    }
}

@Composable
private fun Recommendation(
    recommendation: Recommendation,
    discoverInteractions: DiscoverInteractions,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        discoverInteractions.onRecommendationViewed(recommendationId = recommendation.id)
    }
    NoRipple {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    discoverInteractions.onRecommendationClicked(recommendation.id)
                    CustomTabsIntent
                        .Builder()
                        .build()
                        .launchUrl(
                            context,
                            recommendation.url.toUri(),
                        )
                },
        ) {
            Row {
                Column(
                    modifier = Modifier
                        .weight(2f)
                        .semantics { isTraversalGroup = true },
                ) {
                    Text(
                        text = recommendation.publisher,
                        style = FfTheme.typography.bodySmall,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = recommendation.title,
                        style = FfTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = recommendation.excerpt,
                        style = FfTheme.typography.bodyMedium,
                    )
                }
                AsyncImage(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(FfRadius.media)),
                    model = recommendation.image.firstOrNull()?.url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }
            Row {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    shareUrl(recommendation.url, context)
                    discoverInteractions.onShareClicked(recommendation.id)
                }) {
                    Icon(
                        painter = FfIcons.share(),
                        contentDescription = stringResource(id = R.string.share_content_description),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DiscoverScreenPreview() {
    PreviewTheme {
        DiscoverScreen(
            recommendations = Resource.Loading(),
            discoverInteractions = DiscoverInteractionsNoOp,
        )
    }
}

@file:Suppress("detekt:all")
package org.mozilla.social.feature.discover

import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.common.Resource
import org.mozilla.social.core.designsystem.component.MoSoDivider
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoRadius
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.designsystem.utils.NoRipple
import org.mozilla.social.core.ui.shareUrl
import org.mozilla.social.model.Recommendation

@Composable
internal fun DiscoverScreen(
    viewModel: DiscoverViewModel = koinViewModel()
) {
    DiscoverScreen(
        recommendations = viewModel.recommendations.collectAsState().value,
        discoverInteractions = viewModel,
    )
}

@Composable
private fun DiscoverScreen(
    recommendations: Resource<List<Recommendation>>,
    discoverInteractions: DiscoverInteractions,
) {
    MoSoSurface(
        modifier = Modifier.fillMaxSize()
    ) {
        when (recommendations) {
            is Resource.Loaded -> {
                MainContent(
                    recommendations = recommendations.data,
                    discoverInteractions = discoverInteractions,
                )
            }
            is Resource.Loading -> {}
            is Resource.Error -> {}
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
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp),
                text = stringResource(id = R.string.discover_title),
                style = MoSoTheme.typography.titleLarge,
            )
        }
        items(
            count = recommendations.size,
            key = { recommendations[it].id }
        ) {index ->
            Recommendation(
                recommendation = recommendations[index],
                discoverInteractions = discoverInteractions,
            )
            if (index < recommendations.size - 1) {
                MoSoDivider()
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
    NoRipple {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    CustomTabsIntent
                        .Builder()
                        .build()
                        .launchUrl(
                            context,
                            recommendation.url.toUri(),
                        )
                }
        ) {
            Row {
                Column(
                    modifier = Modifier
                        .weight(2f),
                ) {
                    Text(
                        text = recommendation.publisher,
                        style = MoSoTheme.typography.bodySmall,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = recommendation.title,
                        style = MoSoTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = recommendation.excerpt,
                        style = MoSoTheme.typography.bodyMedium,
                    )
                }
                AsyncImage(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(MoSoRadius.media)),
                    model = recommendation.image.firstOrNull()?.url,
                    contentDescription = recommendation.title,
                    contentScale = ContentScale.Crop,
                )
            }
            Row {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { discoverInteractions.onRepostClicked() }) {
                    Icon(
                        painter = MoSoIcons.boost(),
                        contentDescription = stringResource(id = R.string.repost_content_description),
                    )
                }
                IconButton(onClick = { discoverInteractions.onBookmarkClicked() }) {
                    Icon(
                        painter = MoSoIcons.bookmarkBorder(),
                        contentDescription = stringResource(id = R.string.bookmark_content_description),
                    )
                }
                IconButton(onClick = {
                    shareUrl(recommendation.url, context)
                    discoverInteractions.onShareClicked()
                }) {
                    Icon(
                        painter = MoSoIcons.share(),
                        contentDescription = stringResource(id = R.string.share_content_description),
                    )
                }
            }
        }
    }
}
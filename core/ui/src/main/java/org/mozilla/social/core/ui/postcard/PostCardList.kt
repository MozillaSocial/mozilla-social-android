package org.mozilla.social.core.ui.postcard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.model.Recommendation

@Composable
fun PostCardList(
    feed: Flow<PagingData<PostCardUiState>>,
    reccs: List<Recommendation>?,
    postCardInteractions: PostCardInteractions,
) {

    val lazyingPagingItems: LazyPagingItems<PostCardUiState> = feed.collectAsLazyPagingItems()

    val openAlertDialog = remember { mutableStateOf(false) }

    if (openAlertDialog.value) {
        MoreInfoDialog(
            onDismissRequest = { openAlertDialog.value = false },
            onConfirmation = { openAlertDialog.value = false },
            dialogTitle = "Why am I seeing this?",
            dialogText = "You're seeing this because it's the same thing we show to everyone.",
        )
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(4.dp),
    ) {

        reccs?.let {
            item {
                RecommendationCarousel(reccs = it) { openAlertDialog.value = true }
            }
        }

        items(
            count = lazyingPagingItems.itemCount,
            key = lazyingPagingItems.itemKey { it.statusId }
        ) { index ->
            lazyingPagingItems[index]?.let { item ->
                PostCard(post = item, postCardInteractions)
                if (index < lazyingPagingItems.itemCount) {
                    Divider()
                }
            }
        }
    }
}

@Composable
fun RecommendationCarousel(reccs: List<Recommendation>, onMoreInfoClicked: () -> Unit) {
    val configuration = LocalConfiguration.current

    val widthInDp = configuration.screenWidthDp.dp
    val carouselItemWidth = widthInDp.div(1.2f)

    Column {
        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        ) {
            Text(
                text = "Recommended for you",
                style = org.mozilla.social.core.designsystem.theme.Typography.labelLarge,
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onMoreInfoClicked) {
                Icon(
                    imageVector = MoSoIcons.Info,
                    contentDescription = "more info",
                    tint = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                )
            }
        }

        LazyRow(modifier = Modifier.padding(bottom = 8.dp)) {
            items(count = reccs.size) {
                RecommendationCarouselCard(
                    modifier = Modifier.width(carouselItemWidth),
                    recommendation = reccs[it]
                )
            }
        }
        TextButton(onClick = { /*TODO*/ }) {
            Row(
                androidx.compose.ui.Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            ) {
                Text(
                    text = "Discover More",
                    color = org.mozilla.social.core.designsystem.theme.FirefoxColor.Violet70
                )
                Icon(
                    imageVector = MoSoIcons.ChevronRight,
                    tint = org.mozilla.social.core.designsystem.theme.FirefoxColor.Violet70,
                    contentDescription = null
                )
            }
        }
        Divider()
    }
}

@Composable
fun MoreInfoDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Ok")
            }
        },
    )
}


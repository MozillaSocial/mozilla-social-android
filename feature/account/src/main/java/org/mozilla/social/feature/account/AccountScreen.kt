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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.component.MoSoTopBar
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.core.ui.postcard.PostCardNavigation
import org.mozilla.social.core.ui.postcontent.PostContent
import org.mozilla.social.core.ui.postcontent.PostContentInteractions
import org.mozilla.social.model.Account

@Composable
internal fun AccountRoute(
    accountId: String?,
    onFollowingClicked: () -> Unit,
    onFollowersClicked: () -> Unit,
    onLoggedOut: () -> Unit,
    onCloseClicked: () -> Unit = {},
    postCardNavigation: PostCardNavigation,
    viewModel: AccountViewModel = koinViewModel(
        parameters = {
            parametersOf(
                accountId,
                onLoggedOut,
                postCardNavigation,
            )
        }
    ),
) {
    val account = viewModel.account.collectAsState(initial = null).value

    account?.let {
        AccountScreen(
            account = it,
            showTopBar = accountId != null,
            onFollowingClicked = onFollowingClicked,
            onFollowersClicked = onFollowersClicked,
            onLogoutClicked = {
                viewModel.onLogoutClicked()
            },
            onCloseClicked = onCloseClicked,
            postContentInteractions = viewModel.postCardDelegate,
        )
    }
}

@Composable
internal fun AccountScreen(
    account: Account,
    showTopBar: Boolean,
    onFollowingClicked: () -> Unit,
    onFollowersClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    postContentInteractions: PostContentInteractions,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {

        if (showTopBar) {
            MoSoTopBar(
                title = account.username,
                onCloseClicked = onCloseClicked,
            )
        }
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
            postContentInteractions = postContentInteractions,
        )
        Spacer(modifier = Modifier.padding(top = 4.dp))
        UserFields(
            account = account,
            postContentInteractions = postContentInteractions,
        )
        QuickFunctions(
            name = R.string.posts,
            numericalValue = account.statusesCount,
            onClick = { /*TODO*/ }
        )
        LogoutText(name = R.string.logout) {
            onLogoutClicked()
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
            text = "Followers: ${account.followersCount}",
        )
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                .clickable { onFollowingClicked() },
            text = "Following: ${account.followingCount}",

        )
    }
}

@Composable
private fun UserBio(
    modifier: Modifier = Modifier,
    account: Account,
    postContentInteractions: PostContentInteractions,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
    ) {
        PostContent(
            mentions = emptyList(),
            htmlText = account.bio,
            postContentInteractions = postContentInteractions
        )
    }
}

@Composable
private fun UserFields(
    account: Account,
    postContentInteractions: PostContentInteractions,
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
                PostContent(
                    mentions = emptyList(),
                    htmlText = "<p>${field.value}</p>",
                    postContentInteractions = postContentInteractions,
                )
            }
            if (index < (account.fields?.size ?: 0) - 1) {
                Divider(
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
private fun LogoutText(
    @StringRes name: Int,
    onClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = onClick
    ) {
        Text(
            text = stringResource(id = name),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 8.dp)
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
    MozillaSocialTheme {
//        AccountScreen("110810174933375392")
    }
}
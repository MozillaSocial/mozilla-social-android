@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.google.android.material.textview.MaterialTextView
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.model.Account

@Composable
internal fun AccountRoute(
    onUserFollowings: () -> Unit,
    onUserFollowers: () -> Unit,
    onLogout: () -> Unit,
    viewModel: AccountViewModel = koinViewModel(parameters = {
        parametersOf(
            onUserFollowers,
            onUserFollowings,
            onLogout
        )}),
) {
    val account = viewModel.account.collectAsState(initial = null).value

    account?.let {
        AccountScreen(
            account = it,
            userFollowing = viewModel::showUsersFollowing,
            userFollowers = viewModel::showUsersFollowers,
            logUserOut = viewModel::logoutUser
        )
    }
}

@Composable
internal fun AccountScreen(
    account: Account,
    userFollowing: () -> Unit,
    userFollowers: () -> Unit,
    logUserOut: () -> Unit
) {
    MozillaSocialTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Row {
                    userImages(account = account)
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    userInfo(account = account, modifier = Modifier.align(Alignment.TopCenter))
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 24.dp)
                ) {
                    userFollow(
                        account = account,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .border(
                                border = BorderStroke(2.dp, Color.Gray),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        followingOnClick = userFollowing,
                        followersOnClick = userFollowers
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    userBio(
                        account = account,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                    )
                }
                Row {
                    userFields(account = account)
                }
                Row {
                    quickFunctions(
                        name = R.string.posts,
                        numericalValue = account.statusesCount,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        onClick = { /*TODO*/ }
                    )
                }
                Row {
                    logoutText(name = R.string.logout) {
                        logUserOut()
                    }
                }
            }
        }
    }
}

@Composable
private fun userImages(account: Account) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        headerAndProfileImages(
            headerImage = account.headerUrl,
            headerStaticUrl = account.headerStaticUrl,
            profileImage = account.avatarUrl,
            profileStaticUrl = account.avatarStaticUrl,
            onHeaderClick = { /*TODO*/ },
            onProfileClick = { /*TODO*/ })
    }
}

@Composable
private fun userInfo(account: Account, modifier: Modifier) {
    val url = account.url.split("/")
    val handle = url.last()
    val instance = "@" + url[url.lastIndex - 1]

    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        ) {
            Row {
                Text(
                    text = account.displayName,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )
            }
            Row {
                Text(text = handle + instance)
            }
        }
    }
}

@Composable
private fun userFollow(
    account: Account,
    modifier: Modifier,
    followersOnClick: () -> Unit,
    followingOnClick: () -> Unit
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(8.dp)
        ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            followersOnClick()
                        }
                ) {
                    Text(
                        text = "Followers: ${account.followersCount}",
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                    )
                }

            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )

                Row(
                    modifier = Modifier
                        .clickable {
                            followingOnClick()
                        }
                ) {
                    Text(
                        text = "Following: ${account.followingCount}",
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                    )
                }
        }
    }
}

@Composable
private fun userBio(account: Account, modifier: Modifier) {
    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .wrapContentSize()
        ) {
            Row {
                val bioText = HtmlCompat.fromHtml(account.bio, 0)
                AndroidView(
                    factory = { MaterialTextView(it) },
                    update = { it.text = bioText },
                    modifier = Modifier
                        .wrapContentHeight(),
                )
            }
        }
    }
}

@Composable
private fun userFields(account: Account) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .width(IntrinsicSize.Max)
            .padding(start = 16.dp, end = 16.dp)
            .border(
                border = BorderStroke(2.dp, Color.Gray),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(start = 8.dp, end = 8.dp)
    ) {
        account.fields?.forEachIndexed { index, field ->
            Surface(
                modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                        Text(
                            text = field.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = field.value,
                            style = MaterialTheme.typography.titleMedium
                        )
                }
            }
            if (index < account.fields?.size!!) {
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
private fun quickFunctions(
    @StringRes name: Int,
    numericalValue: Any,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .border(
                    border = BorderStroke(2.dp, Color.Gray),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
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
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
            )
        }
    }
}

@Composable
private fun logoutText(
    @StringRes name: Int,
    onClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 36.dp, end = 36.dp),
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
private fun overlapObjects(
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
private fun headerAndProfileImages(
    modifier: Modifier = Modifier,
    headerImage: String,
    headerStaticUrl: String,
    profileImage: String,
    profileStaticUrl: String,
    onHeaderClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    overlapObjects(modifier = modifier.fillMaxWidth()) {
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
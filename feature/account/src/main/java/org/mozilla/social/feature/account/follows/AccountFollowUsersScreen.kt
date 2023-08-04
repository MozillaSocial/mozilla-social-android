package org.mozilla.social.feature.account.follows

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.model.Account

@Composable
internal fun AccountFollowUsersScreen(
    followType: FollowType,
    viewModel: AccountFollowUsersViewModel = koinViewModel()
) {
    val accountId = viewModel.accountId
    val following = viewModel.accountFollowing.collectAsState(initial = null).value
    val followers = viewModel.accountFollowers.collectAsState(initial = null).value

    when (followType) {
        FollowType.FOLLOWING -> followers?.let { AccountFollowScreen(accounts = it) }
        else -> following?.let { AccountFollowScreen(accounts = it) }
    }
}

@Composable
internal fun AccountFollowScreen(accounts: List<Account>) {
    MozillaSocialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            accounts.forEach { account ->
                AccountQuickView(account)
            }
        }
    }
}

@Composable
internal fun AccountQuickView(account: Account) {
    val url = account.url.split("/")
    val handle = url.last()
    val instance = "@" + url[url.lastIndex - 1]

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row {
                AsyncImage(
                    model = account.avatarUrl,
                    contentDescription = account.avatarStaticUrl,
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = account.displayName,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = handle + instance,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "${account.followersCount} followers",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.End,
            ) {
                MozillaSocialTheme {
                    Text(
                        text = "Following",
                        color = lightColorScheme().primary,
                        modifier = Modifier
                            .background(
                                color = lightColorScheme().primaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(top = 8.dp, bottom = 8.dp, start = 12.dp, end = 12.dp)
                    )
                }
            }
        }
    }

    Divider(
        color = Color.Gray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp)
    )
}

enum class FollowType {
    FOLLOWING,
    FOLLOWERS
}
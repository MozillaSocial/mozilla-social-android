package social.firefly.post.status

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.divider.FfDivider

@Composable
fun AccountSearchBar(
    accounts: List<Account>,
    statusInteractions: StatusInteractions,
) {
    Column {
        FfDivider(
            color = FfTheme.colors.borderPrimary,
        )
        LazyRow(content = {
            items(accounts.size) { index ->
                val account = accounts[index]
                Box(
                    modifier =
                    Modifier
                        .padding(4.dp)
                        .height(50.dp),
                ) {
                    Row(
                        modifier =
                        Modifier
                            .border(
                                width = 2.dp,
                                color = FfTheme.colors.borderPrimary,
                                shape = RoundedCornerShape(4.dp),
                            )
                            .align(Alignment.Center)
                            .clickable { statusInteractions.onAccountClicked(account.accountId) }
                            .padding(4.dp),
                    ) {
                        AsyncImage(
                            modifier =
                            Modifier
                                .height(32.dp)
                                .width(32.dp)
                                .align(Alignment.CenterVertically)
                                .border(
                                    width = 1.dp,
                                    color = FfTheme.colors.borderPrimary,
                                    shape = CircleShape,
                                )
                                .clip(CircleShape),
                            model = account.profilePicUrl,
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                        )
                        Text(
                            modifier =
                            Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 4.dp, end = 4.dp),
                            text = account.accountId,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
        })
    }
}

@Composable
fun HashtagSearchBar(
    hashTags: List<String>,
    statusInteractions: StatusInteractions,
) {
    Column {
        FfDivider(
            color = FfTheme.colors.borderPrimary,
        )
        LazyRow(content = {
            items(hashTags.size) { index ->
                val hashTag = hashTags[index]
                Box(
                    modifier =
                    Modifier
                        .padding(4.dp)
                        .height(50.dp),
                ) {
                    Row(
                        modifier =
                        Modifier
                            .border(
                                width = 2.dp,
                                color = FfTheme.colors.borderPrimary,
                                shape = RoundedCornerShape(4.dp),
                            )
                            .align(Alignment.Center)
                            .clickable { statusInteractions.onHashtagClicked(hashTag) }
                            .padding(4.dp),
                    ) {
                        Text(
                            modifier =
                            Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 4.dp, end = 4.dp),
                            text = "#$hashTag",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
        })
    }
}

@Preview
@Composable
private fun AccountSearchBarPreview() {
    FfTheme {
        AccountSearchBar(
            accounts =
            listOf(
                Account("account1", ""),
                Account("account2", ""),
            ),
            statusInteractions = object : StatusInteractions {},
        )
    }
}

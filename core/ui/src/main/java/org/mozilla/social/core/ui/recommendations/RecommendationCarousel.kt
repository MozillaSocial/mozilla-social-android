package org.mozilla.social.core.ui.recommendations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.mozilla.social.core.designsystem.component.MoSoDivider
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.FirefoxColor
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.designsystem.theme.Typography
import org.mozilla.social.core.ui.R
import org.mozilla.social.model.Recommendation

@Composable
fun RecommendationCarousel(reccs: List<Recommendation>, onMoreInfoClicked: () -> Unit) {
    val configuration = LocalConfiguration.current

    val widthInDp = configuration.screenWidthDp.dp
    val carouselItemWidth = widthInDp.div(1.2f)

    Column {
        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.recommended_for_you),
                style = Typography.labelLarge,
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onMoreInfoClicked) {
                Icon(
                    painter = MoSoIcons.info(),
                    contentDescription = stringResource(id = R.string.more_info_content_description),
                    tint = MaterialTheme.colorScheme.onPrimary,
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
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(id = R.string.recommendations_discover_more),
                    color = MoSoTheme.colors.actionPrimary
                )
                Icon(
                    painter = MoSoIcons.chevronRight(),
                    tint = MoSoTheme.colors.actionPrimary,
                    contentDescription = null
                )
            }
        }
        MoSoDivider()
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
                Text(stringResource(id = R.string.ok))
            }
        },
    )
}
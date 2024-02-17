package social.firefly.core.ui.postcard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import social.firefly.core.designsystem.theme.MoSoRadius
import social.firefly.core.designsystem.theme.MoSoTheme
import social.firefly.core.ui.common.utils.media
import social.firefly.core.ui.htmlcontent.HtmlContentInteractions

@Composable
fun PreviewCard(
    previewCard: PreviewCard,
    htmlContentInteractions: HtmlContentInteractions,
) {
    val borderShape = RoundedCornerShape(MoSoRadius.media)
    Column(
        modifier =
            Modifier
                .clip(borderShape)
                .border(
                    width = 1.dp,
                    color = MoSoTheme.colors.borderPrimary,
                    shape = borderShape,
                )
                .clickable {
                    htmlContentInteractions.onLinkClicked(previewCard.url)
                },
    ) {
        AsyncImage(
            modifier =
                Modifier
                    .aspectRatio(2f)
                    .background(MoSoTheme.colors.layer2),
            model = previewCard.imageUrl,
            contentDescription = "",
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            previewCard.providerName?.let {
                Text(
                    text = it,
                    style = MoSoTheme.typography.bodySmall,
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = previewCard.title,
                style = MoSoTheme.typography.titleSmall,
            )
        }
    }
}

package org.mozilla.social.feature.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import kotlin.math.max

@Composable
internal fun Header(
    modifier: Modifier = Modifier,
    headerUrl: String,
    avatarUrl: String,
    displayName: String,
    handle: String,
    rightSideContent: @Composable () -> Unit,
) {
    Column {
        HeaderLayout(
            modifier = modifier.fillMaxWidth(),
            headerImage = {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(MoSoTheme.colors.layer2),
                    model = headerUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            },
            profileImage = {
                AsyncImage(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(92.dp)
                        .clip(CircleShape)
                        .border(
                            width = 3.dp,
                            color = MoSoTheme.colors.layer1,
                            shape = CircleShape
                        )
                        .background(MoSoTheme.colors.layer2),
                    model = avatarUrl,
                    contentDescription = null,
                )
            },
            rightSideContent = rightSideContent,
        )

        UserInfo(displayName = displayName, handle = handle)
    }
}

@Composable
private fun UserInfo(
    displayName: String,
    handle: String,
) {
    Column(
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = displayName,
            style = MoSoTheme.typography.titleLarge
        )
        Text(
            text = handle,
            style = MoSoTheme.typography.labelMedium,
            color = MoSoTheme.colors.textSecondary,
        )
    }
}

/**
 * Layout for the header images and edit / follow buttons
 * Using a layout is nice because it automatically calculates the spacing for the
 * avatar image so you don't have to calculate the value yourself if the avatar size changes.
 */
@Composable
private fun HeaderLayout(
    modifier: Modifier = Modifier,
    headerImage: @Composable () -> Unit,
    profileImage: @Composable () -> Unit,
    rightSideContent: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = {
            Box { headerImage() }
            Box { profileImage() }
            Box { rightSideContent() }
        },
    ) { measurables, constraints ->
        val placeables = measurables.map {
            it.measure(constraints.copy(
                minWidth = 0,
                minHeight = 0,
            ))
        }
        val headerImagePlaceable = placeables[0]
        val profileImagePlaceable = placeables[1]
        val rightSideContentPlaceable = placeables[2]
        layout(
            width = constraints.maxWidth,
            height = headerImagePlaceable.height
                    + max(profileImagePlaceable.height / 2, rightSideContentPlaceable.height),
        ) {
            headerImagePlaceable.placeRelative(
                x = 0,
                y = 0,
            )
            profileImagePlaceable.placeRelative(
                x = 0,
                y = headerImagePlaceable.height - profileImagePlaceable.height / 2
            )
            rightSideContentPlaceable.placeRelative(
                x = (constraints.maxWidth - rightSideContentPlaceable.width),
                y = headerImagePlaceable.height
            )
        }
    }
}
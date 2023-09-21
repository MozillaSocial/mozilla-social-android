package org.mozilla.social.feed

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.postcard.PostCardInteractions
import org.mozilla.social.core.ui.postcard.PostCardList
import org.mozilla.social.core.ui.postcard.PostCardNavigation

@Composable
fun FeedScreen(
    postCardNavigation: PostCardNavigation,
    viewModel: FeedViewModel = koinViewModel(parameters = {
        parametersOf(
            postCardNavigation
        )
    })
) {
    MoSoSurface {
        PostCardList(
            feed = viewModel.feed,
            postCardInteractions = viewModel.postCardDelegate,
            reccs = viewModel.reccs.collectAsState(initial = null).value,
            enablePullToRefresh = true,
        )

        val context = LocalContext.current

        LaunchedEffect(Unit) {
            viewModel.postCardDelegate.errorToastMessage.collect {
                Toast.makeText(context, it.build(context), Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
fun FeedScreenPreviewLight() {
    MoSoTheme(darkTheme = false) {
        MoSoSurface {
            PostCardList(items = listOf(), postCardInteractions = testInteractions)
            Text(text = "test", color = MoSoTheme.colors.textPrimary)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
@Composable
fun FeedScreenPreviewDark() {
    MoSoTheme(darkTheme = true) {
        MoSoSurface {
            PostCardList(items = listOf(), postCardInteractions = testInteractions)
            Text(text = "test", color = MoSoTheme.colors.textPrimary)
        }
    }
}

val testInteractions = object : PostCardInteractions {}
@file:OptIn(ExperimentalMaterial3Api::class)

package ${PACKAGE_NAME}

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ${FEATURE_NAME}Route(
    viewModel: ${FEATURE_NAME}ViewModel = koinViewModel(),
) {
    ${FEATURE_NAME}Screen()
}

@Composable
internal fun ${FEATURE_NAME}Screen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.screen_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(16.dp)
        ) {
        }
    }
}

@Preview
@Composable
fun ${FEATURE_NAME}ScreenPreview() {
    MozillaSocialTheme {
    }
}
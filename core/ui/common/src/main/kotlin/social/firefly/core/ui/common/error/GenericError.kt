package social.firefly.core.ui.common.error

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import social.firefly.core.ui.common.R
import social.firefly.core.ui.common.button.FfButton

@Composable
fun GenericError(
    modifier: Modifier = Modifier,
    onRetryClicked: () -> Unit,
) {
    Column(
        modifier =
        modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentSize(align = Alignment.Center),
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.error_oops),
        )
        FfButton(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            onClick = { onRetryClicked() },
        ) {
            Text(text = stringResource(id = R.string.retry))
        }
    }
}

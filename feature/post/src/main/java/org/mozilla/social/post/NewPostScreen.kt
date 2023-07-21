@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
)

package org.mozilla.social.post

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.common.LoadState
import org.mozilla.social.common.utils.toFile
import org.mozilla.social.core.designsystem.utils.NoIndication
import org.mozilla.social.core.ui.images.ImageToUpload
import org.mozilla.social.core.ui.transparentTextFieldColors
import java.io.File

@Composable
internal fun NewPostRoute(
    onStatusPosted: () -> Unit,
    onCloseClicked: () -> Unit,
    viewModel: NewPostViewModel = koinViewModel(parameters = { parametersOf(onStatusPosted) })
) {
    NewPostScreen(
        statusText = viewModel.statusText.collectAsState().value,
        onStatusTextChanged = viewModel::onStatusTextUpdated,
        onPostClicked = viewModel::onPostClicked,
        onCloseClicked = onCloseClicked,
        sendButtonEnabled = viewModel.sendButtonEnabled.collectAsState().value,
        onImageInserted = viewModel::onImageInserted,
        imageStates = viewModel.imageState.collectAsState().value,
        imageDescriptionTexts = viewModel.imageDescriptions.collectAsState().value,
        onImageDescriptionTextChanged = viewModel::onImageDescriptionTextUpdated,
        onImageRemoved = viewModel::onImageRemoved
    )
}

@Composable
private fun NewPostScreen(
    statusText: String,
    onStatusTextChanged: (String) -> Unit,
    onPostClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    sendButtonEnabled: Boolean,
    onImageInserted: (Uri, File) -> Unit,
    imageStates: Map<Uri, LoadState>,
    imageDescriptionTexts: Map<Uri, String>,
    onImageDescriptionTextChanged: (Uri, String) -> Unit,
    onImageRemoved: (Uri) -> Unit,
) {
    val context = LocalContext.current
    val imageData = remember { mutableStateOf<List<Uri>>(emptyList()) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
        imageData.value = uris
        uris.forEach {
            onImageInserted(it, it.toFile(context))
        }
    }
    Scaffold(
        topBar = { TopBar(
            onPostClicked = onPostClicked,
            onCloseClicked = onCloseClicked,
            sendButtonEnabled = sendButtonEnabled,
        ) },
        bottomBar = { BottomBar(
            onUploadImageClicked = { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
        ) },
    ) {
        MainBox(
            paddingValues = it,
            statusText = statusText,
            onStatusTextChanged = onStatusTextChanged,
            imageListData = imageData,
            imageStates = imageStates,
            onImageInserted = onImageInserted,
            imageDescriptionTexts = imageDescriptionTexts,
            onImageDescriptionTextChanged = onImageDescriptionTextChanged,
            onImageRemoved = onImageRemoved,
        )
    }
}

@Composable
private fun TopBar(
    onPostClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    sendButtonEnabled: Boolean,
) {
    Column {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(
                    onClick = { onCloseClicked() },
                ) {
                    Icon(Icons.Default.Close, "close")
                }
            },
            actions = {
                IconButton(
                    enabled = sendButtonEnabled,
                    onClick = { onPostClicked() },
                ) {
                    Icon(Icons.Default.Send, "post")
                }
            }
        )
        Divider(
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun BottomBar(
    onUploadImageClicked: () -> Unit,
) {
    Column {
        Divider(
            color = MaterialTheme.colorScheme.primary
        )
        BottomAppBar(
            modifier = Modifier.height(60.dp)
        ) {
            IconButton(onClick = { onUploadImageClicked() }) {
                Icon(Icons.Default.Add, "attach image")
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MainBox(
    paddingValues: PaddingValues,
    statusText: String,
    onStatusTextChanged: (String) -> Unit,
    imageListData: MutableState<List<Uri>>,
    imageStates: Map<Uri, LoadState>,
    onImageInserted: (Uri, File) -> Unit,
    imageDescriptionTexts: Map<Uri, String>,
    onImageDescriptionTextChanged: (Uri, String) -> Unit,
    onImageRemoved: (Uri) -> Unit,
) {
    val localIndication = LocalIndication.current
    // disable ripple on click for the background
    CompositionLocalProvider(
        LocalIndication provides NoIndication
    ) {
        val keyboard = LocalSoftwareKeyboardController.current
        val textFieldFocusRequester = remember { FocusRequester() }
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .clickable {
                    textFieldFocusRequester.requestFocus()
                    keyboard?.show()
                }
        ) {
            // re-enable ripple
            CompositionLocalProvider(
                LocalIndication provides localIndication
            ) {
                LazyColumn {
                    item {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(textFieldFocusRequester),
                            value = statusText,
                            onValueChange = onStatusTextChanged,
                            label = {
                                Text(
                                    text = "What's happening?"
                                )
                            },
                            colors = transparentTextFieldColors()
                        )
                    }
                    items(imageListData.value.size) { index ->
                        val imageUri = imageListData.value[index]
                        ImageUploadBox(
                            imageListData = imageListData,
                            imageUri = imageUri,
                            imageState = imageStates[imageUri] ?: LoadState.LOADING,
                            onImageInserted = onImageInserted,
                            imageDescriptionText = imageDescriptionTexts[imageUri] ?: "",
                            onImageDescriptionTextChanged = onImageDescriptionTextChanged,
                            onImageRemoved = onImageRemoved,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ImageUploadBox(
    imageListData: MutableState<List<Uri>>,
    imageUri: Uri,
    imageState: LoadState,
    onImageInserted: (Uri, File) -> Unit,
    imageDescriptionText: String,
    onImageDescriptionTextChanged: (Uri, String) -> Unit,
    onImageRemoved: (Uri) -> Unit,
) {
    val outlineShape = RoundedCornerShape(12.dp)
    Column(
        modifier = Modifier
            .padding(16.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = outlineShape
            )
            .clip(
                outlineShape
            )
            .fillMaxWidth(),
    ) {
        ImageToUpload(
            imageUri = imageUri,
            imageState = imageState,
            onRetryClicked = onImageInserted,
        )
        if (imageState == LoadState.LOADED || imageState == LoadState.ERROR) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (imageState == LoadState.LOADED) {
                    TextField(
                        modifier = Modifier.weight(1f),
                        value = imageDescriptionText,
                        onValueChange = { onImageDescriptionTextChanged(imageUri, it) },
                        label = {
                            Text(
                                text = "Add a description"
                            )
                        },
                        colors = transparentTextFieldColors(),
                    )
                }
                IconButton(
                    onClick = {
                        imageListData.value = imageListData.value.toMutableList().apply {
                            remove(imageUri)
                        }
                        onImageRemoved(imageUri)
                    }
                ) {
                    Icon(Icons.Default.Delete, "delete")
                }
            }
        }
    }
}

//@Preview
//@Composable
//private fun NewPostScreenPreview() {
//    MozillaSocialTheme {
//        NewPostScreen(
//            statusText = "",
//            onStatusTextChanged = {},
//            onPostClicked = {},
//            onCloseClicked = {},
//            sendButtonEnabled = false,
//            onImageInserted = {},
//            imageState = LoadState.LOADED,
//            imageDescriptionText = "",
//            onImageDescriptionTextChanged = {},
//            onImageRemoved = {}
//        )
//    }
//}

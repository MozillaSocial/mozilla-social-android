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
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.common.LoadState
import org.mozilla.social.common.utils.toFile
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.core.designsystem.utils.NoIndication
import org.mozilla.social.core.ui.images.ImageToUpload
import org.mozilla.social.core.ui.transparentTextFieldColors
import org.mozilla.social.model.ImageState
import org.mozilla.social.post.interactions.ImageInteractions

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
        imageStates = viewModel.imageStates.collectAsState().value,
        addImageButtonEnabled = viewModel.addImageButtonEnabled.collectAsState().value,
        imageInteractions = viewModel
    )
}

@Composable
private fun NewPostScreen(
    statusText: String,
    onStatusTextChanged: (String) -> Unit,
    onPostClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    sendButtonEnabled: Boolean,
    imageStates: Map<Uri, ImageState>,
    addImageButtonEnabled: Boolean,
    imageInteractions: ImageInteractions,
) {
    val context = LocalContext.current
    val multipleMediaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(
            maxItems = (NewPostViewModel.MAX_IMAGES - imageStates.size).coerceAtLeast(2)
        )
    ) { uris ->
        uris.forEach {
            imageInteractions.onImageInserted(it, it.toFile(context))
        }
    }
    val singleMediaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { imageInteractions.onImageInserted(it, it.toFile(context)) }
    }

    Scaffold(
        topBar = { TopBar(
            onPostClicked = onPostClicked,
            onCloseClicked = onCloseClicked,
            sendButtonEnabled = sendButtonEnabled,
        ) },
        bottomBar = { BottomBar(
            onUploadImageClicked = {
                val mediaRequest = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                if (NewPostViewModel.MAX_IMAGES - imageStates.size <= 1) {
                    singleMediaLauncher.launch(mediaRequest)
                } else {
                    multipleMediaLauncher.launch(mediaRequest)
                }
            },
            addImageButtonEnabled = addImageButtonEnabled,
        ) },
    ) {
        MainBox(
            paddingValues = it,
            statusText = statusText,
            onStatusTextChanged = onStatusTextChanged,
            imageStates = imageStates,
            imageInteractions = imageInteractions,
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
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Composable
private fun BottomBar(
    onUploadImageClicked: () -> Unit,
    addImageButtonEnabled: Boolean,
) {
    Column {
        Divider(
            color = MaterialTheme.colorScheme.outlineVariant
        )
        BottomAppBar(
            modifier = Modifier.height(60.dp)
        ) {
            IconButton(
                onClick = { onUploadImageClicked() },
                enabled = addImageButtonEnabled,
            ) {
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
    imageStates: Map<Uri, ImageState>,
    imageInteractions: ImageInteractions,
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
                    items(imageStates.size) { index ->
                        ImageUploadBox(
                            imageState = imageStates.entries.elementAt(index),
                            imageInteractions = imageInteractions,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ImageUploadBox(
    imageState: Map.Entry<Uri, ImageState>,
    imageInteractions: ImageInteractions,
) {
    val outlineShape = RoundedCornerShape(12.dp)
    Column(
        modifier = Modifier
            .padding(16.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = outlineShape
            )
            .clip(
                outlineShape
            )
            .fillMaxWidth(),
    ) {
        ImageToUpload(
            imageUri = imageState.key,
            loadState = imageState.value.loadState,
            onRetryClicked = imageInteractions::onImageInserted,
        )
        if (imageState.value.loadState == LoadState.LOADED || imageState.value.loadState == LoadState.ERROR) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (imageState.value.loadState == LoadState.LOADED) {
                    TextField(
                        modifier = Modifier.weight(1f),
                        value = imageState.value.description,
                        onValueChange = { imageInteractions.onImageDescriptionTextUpdated(imageState.key, it) },
                        label = {
                            Text(
                                text = "Add a description"
                            )
                        },
                        colors = transparentTextFieldColors(),
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
                IconButton(
                    onClick = {
                        imageInteractions.onImageRemoved(imageState.key)
                    }
                ) {
                    Icon(Icons.Default.Delete, "delete")
                }
            }
        }
    }
}

@Preview
@Composable
private fun NewPostScreenPreview() {
    MozillaSocialTheme {
        NewPostScreen(
            statusText = "",
            onStatusTextChanged = {},
            onPostClicked = {},
            onCloseClicked = {},
            sendButtonEnabled = true,
            imageStates = mapOf(),
            addImageButtonEnabled = true,
            imageInteractions = object : ImageInteractions {}
        )
    }
}

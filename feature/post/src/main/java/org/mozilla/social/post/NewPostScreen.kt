@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class,
)

package org.mozilla.social.post

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.common.utils.toFile
import org.mozilla.social.core.designsystem.theme.FirefoxColor
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.core.designsystem.utils.NoIndication
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
        imageState = viewModel.imageState.collectAsState().value,
        imageDescriptionText = viewModel.imageDescription.collectAsState().value,
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
    onImageInserted: (File) -> Unit,
    imageState: ImageState,
    imageDescriptionText: String,
    onImageDescriptionTextChanged: (String) -> Unit,
    onImageRemoved: () -> Unit,
) {
    val context = LocalContext.current
    val imageData = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageData.value = uri
        uri?.let { onImageInserted(uri.toFile(context)) }
    }
    Scaffold(
        topBar = { TopBar(
            onPostClicked = onPostClicked,
            onCloseClicked = onCloseClicked,
            sendButtonEnabled = sendButtonEnabled,
        ) },
        bottomBar = { BottomBar(
            onUploadImageClicked = { launcher.launch("image/*") }
        ) },
    ) {
        MainBox(
            paddingValues = it,
            statusText = statusText,
            onStatusTextChanged = onStatusTextChanged,
            imageData = imageData,
            imageState = imageState,
            onImageInserted = onImageInserted,
            imageDescriptionText = imageDescriptionText,
            onImageDescriptionTextChanged = onImageDescriptionTextChanged,
            onImageRemoved = onImageRemoved,
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MainBox(
    paddingValues: PaddingValues,
    statusText: String,
    onStatusTextChanged: (String) -> Unit,
    imageData: MutableState<Uri?>,
    imageState: ImageState,
    onImageInserted: (File) -> Unit,
    imageDescriptionText: String,
    onImageDescriptionTextChanged: (String) -> Unit,
    onImageRemoved: () -> Unit,
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
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
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
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        )
                    )
                    ImageUploadBox(
                        imageData = imageData,
                        imageState = imageState,
                        onImageInserted = onImageInserted,
                        imageDescriptionText = imageDescriptionText,
                        onImageDescriptionTextChanged = onImageDescriptionTextChanged,
                        onImageRemoved = onImageRemoved,
                    )
                }
            }
        }
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

@Composable
private fun ImageUploadBox(
    imageData: MutableState<Uri?>,
    imageState: ImageState,
    onImageInserted: (File) -> Unit,
    imageDescriptionText: String,
    onImageDescriptionTextChanged: (String) -> Unit,
    onImageRemoved: () -> Unit,
) {
    if (imageData.value == null) return
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
            ),
    ) {
        ImageToUpload(
            imageData = imageData,
            imageState = imageState,
            onImageInserted = onImageInserted,
        )
        if (imageState == ImageState.LOADED || imageState == ImageState.ERROR) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (imageState == ImageState.LOADED) {
                    TextField(
                        modifier = Modifier.weight(1f),
                        value = imageDescriptionText,
                        onValueChange = { onImageDescriptionTextChanged(it) },
                        label = {
                            Text(
                                text = "Add a description"
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                    )
                }
                IconButton(
                    onClick = {
                        imageData.value = null
                        onImageRemoved()
                    }
                ) {
                    Icon(Icons.Default.Delete, "delete")
                }
            }
        }
    }
}

@Composable
private fun ImageToUpload(
    imageData: MutableState<Uri?>,
    imageState: ImageState,
    onImageInserted: (File) -> Unit,
) {
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    imageData.value?.let { uri ->
        if (Build.VERSION.SDK_INT < 28) {
            bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            bitmap.value = ImageDecoder.decodeBitmap(source)
        }
        bitmap.value?.let { bitmap ->
            when (imageState) {
                ImageState.LOADING -> {
                    Box(
                        modifier = Modifier
                            .background(FirefoxColor.Black)
                    ) {
                        Image(
                            modifier = Modifier.alpha(.3f),
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = null,
                        )
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                ImageState.LOADED -> {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                    )
                }
                ImageState.ERROR -> {
                    Box(
                        modifier = Modifier
                            .background(FirefoxColor.Black)
                    ) {
                        Image(
                            modifier = Modifier.alpha(.3f),
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = null,
                        )
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                        ) {
                            Text(
                                text = "Failed to upload image",
                                color = FirefoxColor.White,
                            )
                            Button(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(16.dp),
                                onClick = {
                                    imageData.value?.let {
                                        onImageInserted(uri.toFile(context))
                                    }
                                }
                            ) {
                                Text(
                                    text = "Retry",
                                    color = FirefoxColor.White,
                                )
                            }
                        }
                    }
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
            sendButtonEnabled = false,
            onImageInserted = {},
            imageState = ImageState.LOADED,
            imageDescriptionText = "",
            onImageDescriptionTextChanged = {},
            onImageRemoved = {}
        )
    }
}

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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.common.LoadState
import org.mozilla.social.common.utils.buildAnnotatedStringForAccountsAndHashtags
import org.mozilla.social.common.utils.toFile
import org.mozilla.social.core.designsystem.component.MoSoButton
import org.mozilla.social.core.designsystem.component.MoSoDivider
import org.mozilla.social.core.designsystem.component.MoSoDropdownMenu
import org.mozilla.social.core.designsystem.component.MoSoDropdownMenuItem
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.component.MoSoTextField
import org.mozilla.social.core.designsystem.component.MoSoToast
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.FirefoxColor
import org.mozilla.social.core.designsystem.theme.MoSoSpacing
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.designsystem.utils.NoIndication
import org.mozilla.social.core.ui.TransparentNoTouchOverlay
import org.mozilla.social.core.ui.VerticalDivider
import org.mozilla.social.core.ui.VisibilityDropDownButton
import org.mozilla.social.core.ui.media.MediaUpload
import org.mozilla.social.core.ui.transparentTextFieldColors
import org.mozilla.social.feature.post.R
import org.mozilla.social.model.ImageState
import org.mozilla.social.model.StatusVisibility
import org.mozilla.social.post.NewPostViewModel.Companion.MAX_POLL_COUNT
import org.mozilla.social.post.NewPostViewModel.Companion.MAX_POST_LENGTH
import org.mozilla.social.post.NewPostViewModel.Companion.MIN_POLL_COUNT
import org.mozilla.social.post.media.MediaInteractions
import org.mozilla.social.post.poll.Poll
import org.mozilla.social.post.poll.PollDuration
import org.mozilla.social.post.poll.PollDurationDropDown
import org.mozilla.social.post.poll.PollInteractions
import org.mozilla.social.post.poll.PollStyle
import org.mozilla.social.post.poll.PollStyleDropDown
import org.mozilla.social.post.status.Account
import org.mozilla.social.post.status.AccountSearchBar
import org.mozilla.social.post.status.ContentWarningInteractions
import org.mozilla.social.post.status.HashtagSearchBar
import org.mozilla.social.post.status.StatusInteractions

@Composable
internal fun NewPostScreen(
    onStatusPosted: () -> Unit,
    onCloseClicked: () -> Unit,
    replyStatusId: String?,
    viewModel: NewPostViewModel = koinViewModel(parameters = {
        parametersOf(
            onStatusPosted,
            replyStatusId,
        )
    })
) {
    NewPostScreen(
        statusText = viewModel.statusText.collectAsState().value,
        statusInteractions = viewModel.statusInteractions,
        onPostClicked = viewModel::onPostClicked,
        onCloseClicked = onCloseClicked,
        sendButtonEnabled = viewModel.sendButtonEnabled.collectAsState().value,
        imageStates = viewModel.imageStates.collectAsState().value,
        addImageButtonEnabled = viewModel.addImageButtonEnabled.collectAsState().value,
        mediaInteractions = viewModel.mediaInteractions,
        isSendingPost = viewModel.isSendingPost.collectAsState().value,
        visibility = viewModel.visibility.collectAsState().value,
        onVisibilitySelected = viewModel::onVisibilitySelected,
        poll = viewModel.poll.collectAsState().value,
        pollInteractions = viewModel.pollInteractions,
        pollButtonEnabled = viewModel.pollButtonEnabled.collectAsState().value,
        contentWarningText = viewModel.contentWarningText.collectAsState().value,
        contentWarningInteractions = viewModel.contentWarningInteractions,
        accounts = viewModel.accountList.collectAsState().value,
        hashTags = viewModel.hashtagList.collectAsState().value,
        inReplyToAccountName = viewModel.inReplyToAccountName.collectAsState().value,
        userHeaderState = viewModel.userHeaderState.collectAsState(initial = null).value,
    )

    MoSoToast(toastMessage = viewModel.errorToastMessage)
}

data class UserHeaderState(val avatarUrl: String, val displayName: String)

@Composable
private fun NewPostScreen(
    statusText: TextFieldValue,
    statusInteractions: StatusInteractions,
    onPostClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    sendButtonEnabled: Boolean,
    imageStates: Map<Uri, ImageState>,
    addImageButtonEnabled: Boolean,
    mediaInteractions: MediaInteractions,
    isSendingPost: Boolean,
    visibility: StatusVisibility,
    onVisibilitySelected: (StatusVisibility) -> Unit,
    poll: Poll?,
    pollInteractions: PollInteractions,
    pollButtonEnabled: Boolean,
    contentWarningText: String?,
    contentWarningInteractions: ContentWarningInteractions,
    accounts: List<Account>?,
    hashTags: List<String>?,
    inReplyToAccountName: String?,
    userHeaderState: UserHeaderState?,
) {
    val context = LocalContext.current
    val multipleMediaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(
            maxItems = (NewPostViewModel.MAX_IMAGES - imageStates.size).coerceAtLeast(2)
        )
    ) { uris ->
        uris.forEach {
            mediaInteractions.onMediaInserted(it, it.toFile(context))
        }
    }
    val singleMediaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { mediaInteractions.onMediaInserted(it, it.toFile(context)) }
    }
    Box(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.ime.exclude(WindowInsets.navigationBars))
            .background(MoSoTheme.colors.layer1)
    ) {
        Column {
            TopBar(
                onPostClicked = onPostClicked,
                onCloseClicked = onCloseClicked,
                sendButtonEnabled = sendButtonEnabled,
            )
            userHeaderState?.let { userHeaderState ->
                UserHeader(
                    userHeaderState = userHeaderState,
                    visibility = visibility,
                    onVisibilitySelected = onVisibilitySelected,
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                MainBox(
                    statusText = statusText,
                    statusInteractions = statusInteractions,
                    imageStates = imageStates,
                    mediaInteractions = mediaInteractions,
                    poll = poll,
                    pollInteractions = pollInteractions,
                    contentWarningText = contentWarningText,
                    contentWarningInteractions = contentWarningInteractions,
                    inReplyToAccountName = inReplyToAccountName,
                )
            }
            accounts?.let {
                AccountSearchBar(accounts = accounts, statusInteractions = statusInteractions)
            }
            hashTags?.let {
                HashtagSearchBar(hashTags = hashTags, statusInteractions = statusInteractions)
            }
            BottomBar(
                onUploadImageClicked = {
                    val mediaRequest =
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    if (NewPostViewModel.MAX_IMAGES - imageStates.size <= 1) {
                        singleMediaLauncher.launch(mediaRequest)
                    } else {
                        multipleMediaLauncher.launch(mediaRequest)
                    }
                },
                onUploadVideoClicked = {
                    val mediaRequest =
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                    if (NewPostViewModel.MAX_IMAGES - imageStates.size <= 1) {
                        singleMediaLauncher.launch(mediaRequest)
                    } else {
                        multipleMediaLauncher.launch(mediaRequest)
                    }
                },
                addImageButtonEnabled = addImageButtonEnabled,
                statusText = statusText.text,
                contentWarningText = contentWarningText,
                pollInteractions = pollInteractions,
                pollButtonEnabled = pollButtonEnabled,
                contentWarningInteractions = contentWarningInteractions,
            )
        }
        if (isSendingPost) {
            TransparentNoTouchOverlay()
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun UserHeader(
    userHeaderState: UserHeaderState,
    visibility: StatusVisibility,
    onVisibilitySelected: (StatusVisibility) -> Unit
) {
    Row {
        AsyncImage(
            modifier = Modifier
                .padding(horizontal = MoSoSpacing.sm)
                .size(92.dp)
                .clip(CircleShape)
                .border(
                    width = 3.dp,
                    color = MoSoTheme.colors.layer1,
                    shape = CircleShape
                ),
            model = userHeaderState.avatarUrl,
            contentDescription = null,
        )

        Column {
            Text(text = userHeaderState.displayName, style = MoSoTheme.typography.labelMedium)

            VisibilityDropDownButton(
                visibility = visibility,
                onVisibilitySelected = onVisibilitySelected,
            )
        }

    }
}

@Composable
private fun TopBar(
    onPostClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    sendButtonEnabled: Boolean,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(MoSoSpacing.sm),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        // left side
        IconButton(
            onClick = { onCloseClicked() },
        ) {
            Icon(
                MoSoIcons.x(),
                "close",
                tint = MoSoTheme.colors.textPrimary,
            )
        }

        Spacer(modifier = Modifier.padding(start = 16.dp))
        MoSoButton(onClick = onPostClicked, enabled = sendButtonEnabled) {
            Text(
                text = stringResource(id = R.string.post),
                style = MoSoTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun BottomBar(
    onUploadImageClicked: () -> Unit,
    onUploadVideoClicked: () -> Unit,
    addImageButtonEnabled: Boolean,
    statusText: String,
    contentWarningText: String?,
    pollInteractions: PollInteractions,
    pollButtonEnabled: Boolean,
    contentWarningInteractions: ContentWarningInteractions,
) {
    val characterCountText = remember(statusText, contentWarningText) {
        "${MAX_POST_LENGTH - statusText.length - (contentWarningText?.length ?: 0)}"
    }
    Column {
        MoSoDivider(
            color = MoSoTheme.colors.borderPrimary
        )
        Row(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
                .background(MoSoTheme.colors.layer1),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PhotoVideoImportButton(
                addImageButtonEnabled = addImageButtonEnabled,
                onUploadImageClicked = onUploadImageClicked
            )
            Spacer(modifier = Modifier.width(MoSoSpacing.sm))
            AddPollButton(
                pollInteractions = pollInteractions,
                pollButtonEnabled = pollButtonEnabled
            )
            Spacer(modifier = Modifier.width(MoSoSpacing.sm))
            ContentWarningButton(
                contentWarningInteractions = contentWarningInteractions,
                contentWarningText = contentWarningText,
            )
            Spacer(modifier = Modifier.weight(1f))
            CharacterCountLabel(characterCountText = characterCountText)
        }
    }
}

@Composable
fun PhotoVideoImportButton(addImageButtonEnabled: Boolean, onUploadImageClicked: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        MoSoDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
        ) {
            MoSoDropdownMenuItem(
                text = {
                    Row(Modifier.wrapContentSize()) {
                        Icon(
                            MoSoIcons.image(),
                            contentDescription = stringResource(id = R.string.add_photos_content_description),
                        )
                        Spacer(modifier = Modifier.width(MoSoSpacing.sm))
                        Text(text = stringResource(id = R.string.photo))
                    }
                },
                onClick = {
                    expanded = false
                    onUploadImageClicked()
                }
            )

            MoSoDropdownMenuItem(
                text = {
                    Row(Modifier.wrapContentSize()) {
                        Icon(
                            MoSoIcons.monitorPlay(),
                            contentDescription = stringResource(id = R.string.add_video_content_description),
                        )

                        Spacer(modifier = Modifier.width(MoSoSpacing.sm))
                        Text(text = stringResource(id = R.string.video))
                    }
                },
                onClick = {
                    expanded = false
                    onUploadImageClicked()
                }
            )

        }


        IconButton(
            onClick = {
                expanded = true
            },

            enabled = addImageButtonEnabled,
        ) {
            Icon(
                MoSoIcons.imagePlus(),
                stringResource(id = R.string.attach_image_button_content_description),
                tint = MoSoTheme.colors.textPrimary,
            )
        }
    }
}

@Composable
fun AddPollButton(pollInteractions: PollInteractions, pollButtonEnabled: Boolean) {
    IconButton(
        onClick = { pollInteractions.onNewPollClicked() },
        enabled = pollButtonEnabled,
    ) {
        Icon(
            MoSoIcons.chartBar(),
            stringResource(id = R.string.add_poll_button_content_description),
            tint = MoSoTheme.colors.textPrimary,
        )
    }
}

@Composable
fun ContentWarningButton(
    contentWarningInteractions: ContentWarningInteractions,
    contentWarningText: String?,
) {
    IconButton(
        onClick = { contentWarningInteractions.onContentWarningClicked() },
    ) {
        Icon(
            MoSoIcons.warning(),
            stringResource(id = R.string.content_warning_button_content_description),
            tint = if (contentWarningText == null) {
                LocalContentColor.current
            } else {
                MoSoTheme.colors.textWarning
            },
        )
    }
}

@Composable
fun CharacterCountLabel(characterCountText: String) {
    Text(
        modifier = Modifier
            .wrapContentSize()
            .padding(MoSoSpacing.md),
        text = characterCountText,
        style = MoSoTheme.typography.labelSmall,
        color = MoSoTheme.colors.textSecondary,
    )

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MainBox(
    statusText: TextFieldValue,
    statusInteractions: StatusInteractions,
    imageStates: Map<Uri, ImageState>,
    mediaInteractions: MediaInteractions,
    poll: Poll?,
    pollInteractions: PollInteractions,
    contentWarningText: String?,
    contentWarningInteractions: ContentWarningInteractions,
    inReplyToAccountName: String?,
) {
    val localIndication = LocalIndication.current
    // disable ripple on click for the background
    CompositionLocalProvider(
        LocalIndication provides NoIndication
    ) {
        val keyboard = LocalSoftwareKeyboardController.current

        val textFieldFocusRequester = remember { FocusRequester() }
        MoSoSurface(
            modifier = Modifier
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
                        InReplyToText(inReplyToAccountName = inReplyToAccountName)
                    }
                    contentWarningText?.let {
                        item {
                            ContentWarningEntry(contentWarningText, contentWarningInteractions)
                        }
                    }

                    item {
                        val highlightColor = FirefoxColor.Blue60
                        MoSoTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(textFieldFocusRequester),
                            value = statusText,
                            onValueChange = { statusInteractions.onStatusTextUpdated(it) },
                            label = {
                                Text(
                                    text = stringResource(id = R.string.new_post_text_field_label)
                                )
                            },
                            visualTransformation = {
                                TransformedText(
                                    buildAnnotatedStringForAccountsAndHashtags(
                                        it.text,
                                        SpanStyle(
                                            color = highlightColor
                                        )
                                    ),
                                    OffsetMapping.Identity
                                )
                            }
                        )
                        LaunchedEffect(Unit) {
                            textFieldFocusRequester.requestFocus()
                        }
                    }

                    poll?.let {
                        items(poll.options.size) { index ->
                            PollChoice(
                                index = index,
                                poll = poll,
                                pollInteractions = pollInteractions
                            )
                        }
                        item { PollSettings(poll = poll, pollInteractions = pollInteractions) }
                    }

                    items(imageStates.size) { index ->
                        ImageUploadBox(
                            imageState = imageStates.entries.elementAt(index),
                            mediaInteractions = mediaInteractions,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InReplyToText(
    inReplyToAccountName: String?,
) {
    if (inReplyToAccountName != null) {
        Row(
            modifier = Modifier.padding(start = 12.dp),
        ) {
            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterVertically),
                painter = MoSoIcons.chatBubbles(),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.padding(start = 8.dp))
            Text(
                text = stringResource(
                    id = R.string.in_reply_to_account_name_label,
                    inReplyToAccountName
                ),
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
private fun ContentWarningEntry(
    contentWarningText: String,
    contentWarningInteractions: ContentWarningInteractions,
) {
    OutlinedTextField(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        value = contentWarningText,
        onValueChange = { contentWarningInteractions.onContentWarningTextChanged(it) },
        label = { Text(text = stringResource(id = R.string.content_warning_label)) },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MoSoTheme.colors.borderWarning,
            focusedBorderColor = MoSoTheme.colors.borderWarning,
            focusedLabelColor = MoSoTheme.colors.textWarning,
            unfocusedLabelColor = MoSoTheme.colors.textWarning,
            cursorColor = MoSoTheme.colors.textWarning,
        )
    )
}

@Composable
private fun ImageUploadBox(
    imageState: Map.Entry<Uri, ImageState>,
    mediaInteractions: MediaInteractions,
) {
    val outlineShape = RoundedCornerShape(12.dp)
    Column(
        modifier = Modifier
            .padding(16.dp)
            .border(
                width = 2.dp,
                color = MoSoTheme.colors.borderPrimary,
                shape = outlineShape
            )
            .clip(
                outlineShape
            )
            .fillMaxWidth(),
    ) {
        MediaUpload(
            uri = imageState.key,
            loadState = imageState.value.loadState,
            onRetryClicked = mediaInteractions::onMediaInserted,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (imageState.value.loadState == LoadState.LOADED) {
                TextField(
                    modifier = Modifier.weight(1f),
                    value = imageState.value.description,
                    onValueChange = {
                        mediaInteractions.onMediaDescriptionTextUpdated(
                            imageState.key,
                            it
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.media_alt_text_label)
                        )
                    },
                    colors = transparentTextFieldColors(),
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
            IconButton(
                onClick = {
                    mediaInteractions.onDeleteMediaClicked(imageState.key)
                }
            ) {
                Icon(
                    MoSoIcons.delete(),
                    stringResource(id = R.string.delete_button_content_description)
                )
            }
        }
    }
}

@Composable
private fun PollChoice(
    index: Int,
    poll: Poll,
    pollInteractions: PollInteractions,
) {
    val deleteEnabled = remember(poll) { poll.options.size > MIN_POLL_COUNT }
    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            value = poll.options[index],
            onValueChange = { pollInteractions.onPollOptionTextChanged(index, it) },
            label = {
                Text(
                    text = stringResource(id = R.string.poll_choice_label, index + 1)
                )
            }
        )
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            onClick = {
                pollInteractions.onPollOptionDeleteClicked(index)
            },
            enabled = deleteEnabled
        ) {
            Icon(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp),
                painter = MoSoIcons.deleteOutline(),
                contentDescription = stringResource(id = R.string.remove_poll_option_button_content_description),
            )
        }
    }
}

@Composable
private fun PollSettings(
    poll: Poll,
    pollInteractions: PollInteractions,
) {
    Row(
        modifier = Modifier
            .padding(start = 8.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
    ) {
        val addNewOptionEnabled = remember(poll) { poll.options.size < MAX_POLL_COUNT }
        IconButton(
            onClick = {
                pollInteractions.onAddPollOptionClicked()
            },
            enabled = addNewOptionEnabled,
        ) {
            Icon(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp),
                painter = MoSoIcons.addCircleOutline(),
                contentDescription = stringResource(id = R.string.add_poll_option_button_content_description),
            )
        }

        Row(
            modifier = Modifier.padding(top = 4.dp)
        ) {
            VerticalDivider(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .height(40.dp)
            )
            PollDurationDropDown(poll = poll, pollInteractions = pollInteractions)
            VerticalDivider(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .height(40.dp)
            )
            PollStyleDropDown(poll = poll, pollInteractions = pollInteractions)
        }
    }
    Row(
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp, end = 8.dp)
            .clickable { pollInteractions.onHideCountUntilEndClicked() }
    ) {
        Checkbox(
            modifier = Modifier.align(Alignment.CenterVertically),
            checked = poll.hideTotals,
            onCheckedChange = { pollInteractions.onHideCountUntilEndClicked() },
        )
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = stringResource(id = R.string.poll_option_hide_results),
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Preview
@Composable
private fun NewPostScreenPreview() {
    MoSoTheme(
        false
    ) {
        NewPostScreen(
            statusText = TextFieldValue(),
            statusInteractions = object : StatusInteractions {},
            onPostClicked = {},
            onCloseClicked = {},
            sendButtonEnabled = true,
            imageStates = mapOf(),
            addImageButtonEnabled = true,
            mediaInteractions = object : MediaInteractions {},
            isSendingPost = false,
            visibility = StatusVisibility.Private,
            onVisibilitySelected = {},
            poll = null,
            pollInteractions = object : PollInteractions {},
            pollButtonEnabled = true,
            contentWarningText = null,
            contentWarningInteractions = object : ContentWarningInteractions {},
            accounts = null,
            hashTags = null,
            inReplyToAccountName = null,
            userHeaderState = UserHeaderState("", "Barack Obama")
        )
    }
}

@Preview
@Composable
private fun NewPostScreenWithPollPreview() {
    MoSoTheme(
        false
    ) {
        NewPostScreen(
            statusText = TextFieldValue(),
            statusInteractions = object : StatusInteractions {},
            onPostClicked = {},
            onCloseClicked = {},
            sendButtonEnabled = true,
            imageStates = mapOf(),
            addImageButtonEnabled = true,
            mediaInteractions = object : MediaInteractions {},
            isSendingPost = false,
            visibility = StatusVisibility.Private,
            onVisibilitySelected = {},
            poll = Poll(
                options = listOf("option 1", "option 2"),
                style = PollStyle.SINGLE_CHOICE,
                pollDuration = PollDuration.ONE_DAY,
                hideTotals = false
            ),
            pollInteractions = object : PollInteractions {},
            pollButtonEnabled = true,
            contentWarningText = null,
            contentWarningInteractions = object : ContentWarningInteractions {},
            accounts = null,
            hashTags = null,
            inReplyToAccountName = null,
            userHeaderState = UserHeaderState("", "Barack Obama"),
        )
    }
}

@Preview
@Composable
private fun NewPostScreenWithContentWarningPreview() {
    MoSoTheme(
        false
    ) {
        NewPostScreen(
            statusText = TextFieldValue(),
            statusInteractions = object : StatusInteractions {},
            onPostClicked = {},
            onCloseClicked = {},
            sendButtonEnabled = true,
            imageStates = mapOf(),
            addImageButtonEnabled = true,
            mediaInteractions = object : MediaInteractions {},
            isSendingPost = false,
            visibility = StatusVisibility.Private,
            onVisibilitySelected = {},
            poll = null,
            pollInteractions = object : PollInteractions {},
            pollButtonEnabled = true,
            contentWarningText = "Content is bad",
            contentWarningInteractions = object : ContentWarningInteractions {},
            accounts = null,
            hashTags = null,
            inReplyToAccountName = null,
            userHeaderState = UserHeaderState("", "Barack Obama"),
        )
    }
}

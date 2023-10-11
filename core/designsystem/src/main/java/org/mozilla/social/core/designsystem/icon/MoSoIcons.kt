package org.mozilla.social.core.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.Message
import androidx.compose.material.icons.rounded.Poll
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.VolumeMute
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import org.mozilla.social.core.designsystem.R

object MoSoIcons {

    @Composable
    fun house() = painterResource(R.drawable.house)

    @Composable
    fun compass() = painterResource(R.drawable.compass)

    @Composable
    fun bookmark() = painterResource(R.drawable.bookmark_simple)
    
    @Composable
    fun userCircle() = painterResource(id = R.drawable.user_circle)

    @Composable
    fun list() = painterResource(id = R.drawable.list)

    @Composable
    fun userJoin() = painterResource(id = R.drawable.user_join)

    @Composable
    fun add() = rememberVectorPainter(image = Icons.Rounded.Add)

    @Composable
    fun check() = rememberVectorPainter(image = Icons.Rounded.Check)

    @Composable
    fun moreVertical() = painterResource(id = R.drawable.more_vertical)

    @Composable
    fun info() = rememberVectorPainter(image = Icons.Rounded.Info)

    @Composable
    fun share() = painterResource(id = R.drawable.share)

    @Composable
    fun chevronRight() = rememberVectorPainter(image = Icons.Rounded.ChevronRight)

    @Composable
    fun caret() = rememberVectorPainter(image = Icons.Rounded.KeyboardArrowUp)

    @Composable
    fun bookmarkBorder() = rememberVectorPainter(image = Icons.Rounded.BookmarkBorder)

    @Composable
    fun reply() = painterResource(id = R.drawable.chat_bubbles)

    @Composable
    fun boost() = painterResource(id = R.drawable.repeat)

    @Composable
    fun heart() = painterResource(id = R.drawable.heart)

    @Composable
    fun public() = rememberVectorPainter(image = Icons.Rounded.Public)

    @Composable
    fun lockOpen() = rememberVectorPainter(image = Icons.Rounded.LockOpen)

    @Composable
    fun lock() = rememberVectorPainter(image = Icons.Rounded.Lock)

    @Composable
    fun message() = rememberVectorPainter(image = Icons.Rounded.Message)

    @Composable
    fun arrowDropDown() = rememberVectorPainter(image = Icons.Rounded.ArrowDropDown)

    @Composable
    fun volumeMute() = rememberVectorPainter(image = Icons.Rounded.VolumeMute)

    @Composable
    fun volumeUp() = rememberVectorPainter(image = Icons.Rounded.VolumeUp)

    @Composable
    fun close() = rememberVectorPainter(image = Icons.Rounded.Close)

    @Composable
    fun send() = rememberVectorPainter(image = Icons.Rounded.Send)

    @Composable
    fun addPhotoAlternate() = rememberVectorPainter(image = Icons.Rounded.AddPhotoAlternate)

    @Composable
    fun poll() = rememberVectorPainter(image = Icons.Rounded.Poll)

    @Composable
    fun warning() = rememberVectorPainter(image = Icons.Rounded.Warning)

    @Composable
    fun delete() = rememberVectorPainter(image = Icons.Rounded.Delete)

    @Composable
    fun deleteOutline() = rememberVectorPainter(image = Icons.Rounded.DeleteOutline)

    @Composable
    fun addCircleOutline() = rememberVectorPainter(image = Icons.Rounded.AddCircleOutline)

}

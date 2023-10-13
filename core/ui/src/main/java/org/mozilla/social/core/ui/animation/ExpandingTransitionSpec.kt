package org.mozilla.social.core.ui.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith

fun expandingTransitionSpec(
    animationDuration: Int = 150
): AnimatedContentTransitionScope<Boolean>.() -> ContentTransform = {
    // expanding
    if (targetState) {
        EnterTransition.None togetherWith ExitTransition.None using
                SizeTransform { _, _ ->
                    keyframes {
                        durationMillis = animationDuration
                    }
                }
    } else { // shrinking
        fadeIn(animationSpec = tween(0, 0)) togetherWith
                fadeOut(animationSpec = tween(animationDuration)) using
                SizeTransform { _, _ ->
                    keyframes {
                        durationMillis = animationDuration
                    }
                }
    }
}
package social.firefly.common.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

fun AnimatedContentTransitionScope<NavBackStackEntry>.ffSlideIn(): EnterTransition =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Up,
        animationSpec = tween(TWEEN_DURATION),
    )

fun AnimatedContentTransitionScope<NavBackStackEntry>.ffSlideOut(): ExitTransition =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Down,
        animationSpec = tween(TWEEN_DURATION),
    )

fun ffFadeIn(): EnterTransition = fadeIn(tween(durationMillis = TWEEN_DURATION))

fun ffFadeOut(): ExitTransition = fadeOut(tween(durationMillis = TWEEN_DURATION))

const val TWEEN_DURATION = 250

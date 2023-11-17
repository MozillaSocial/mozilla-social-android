package org.mozilla.social.common.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

fun AnimatedContentTransitionScope<NavBackStackEntry>.mosoSlideIn(): EnterTransition =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Up,
        animationSpec = tween(TWEEN_DURATION),
    )

fun AnimatedContentTransitionScope<NavBackStackEntry>.mosoSlideOut(): ExitTransition =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Down,
        animationSpec = tween(TWEEN_DURATION),
    )

fun mosoFadeIn(): EnterTransition = fadeIn(tween(durationMillis = TWEEN_DURATION))

fun mosoFadeOut(): ExitTransition = fadeOut(tween(durationMillis = TWEEN_DURATION))

const val TWEEN_DURATION = 250

package com.faigenbloom.famillyspandings.common.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable

@Composable
fun AnimateEnter(
    visible: Boolean,
    openFromLeft: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    val enterTransition = slideInHorizontally(
        initialOffsetX = { if (openFromLeft) -it else it },
        animationSpec = tween(
            durationMillis = 250,
            easing = LinearEasing,
        ),
    )

    val exitTransition = slideOutHorizontally(
        targetOffsetX = { if (openFromLeft) -it else it },
        animationSpec = tween(
            durationMillis = 250,
            easing = LinearEasing,
        ),
    )

    AnimatedVisibility(
        visible = visible,
        enter = enterTransition,
        exit = exitTransition,
        content = content,
    )
}

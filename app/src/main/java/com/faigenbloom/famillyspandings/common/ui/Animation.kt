package com.faigenbloom.famillyspandings.common.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable

@Composable
fun AnimateTabs(
    isLeftTab: Boolean,
    content: @Composable AnimatedContentScope.(targetState: Boolean) -> Unit,
) {
    AnimatedContent(
        targetState = isLeftTab,
        label = "",
        transitionSpec = {
            slideIntoContainer(
                animationSpec = tween(
                    durationMillis = 250,
                    easing = EaseIn,
                ),
                towards = if (targetState) AnimatedContentTransitionScope.SlideDirection.Right else AnimatedContentTransitionScope.SlideDirection.Left,
            ).togetherWith(
                slideOutOfContainer(
                    animationSpec = tween(
                        durationMillis = 250,
                        easing = EaseOut,
                    ),
                    towards = if (targetState) AnimatedContentTransitionScope.SlideDirection.Right else AnimatedContentTransitionScope.SlideDirection.Left,
                ),
            )
        },
        content = content,
    )
}

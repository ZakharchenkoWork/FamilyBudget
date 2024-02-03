package com.faigenbloom.familybudget.common.ui

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

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

@Composable
fun AnimateError(
    animationTrigger: Boolean,
    defaultColor: Color = colorScheme.background,
    errorColor: Color = colorScheme.primary,
    onFinished: (() -> Unit)? = null,
    content: @Composable (backgroundColorState: Color) -> Unit,
) {
    val color = remember { Animatable(defaultColor) }
    if (animationTrigger) {
        LaunchedEffect(Unit) {
            color.animateTo(errorColor, animationSpec = tween(300))
            color.animateTo(defaultColor, animationSpec = tween(700))
            if (color.value == defaultColor && onFinished != null) {
                onFinished()
            }
        }
        content(color.value)

    } else {
        content(defaultColor)
    }
}

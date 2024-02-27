package com.faigenbloom.familybudget.common.ui

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.faigenbloom.familybudget.R

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

@Composable
fun Loading(isShown: Boolean) {
    if (isShown) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever,
        )
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            LottieAnimation(
                modifier = Modifier.fillMaxWidth(0.3f),
                composition = composition,
                progress = { progress },
            )
        }
    }
}

@Composable
fun ErrorAnimatedIcon(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))
    val progress by animateLottieCompositionAsState(
        composition,
    )
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        LottieAnimation(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .aspectRatio(1f),
            composition = composition,
            progress = { progress },
        )
    }
}

@Composable
fun Success(state: AnimationState) {
    if (state.isShown) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))
        val progress by animateLottieCompositionAsState(composition)
        if (progress < 1) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                LottieAnimation(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    composition = composition,
                    progress = { progress },
                )
            }
        } else {
            state.onFinish()
        }
    }
}

data class AnimationState(
    val isShown: Boolean,
    val onFinish: () -> Unit,
)

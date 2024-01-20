package com.faigenbloom.famillyspandings.comon

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

const val MENU_FLOATING_BUTTON = "MENU_FLOATING_BUTTON"
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun FloatingActionMenu(
    modifier: Modifier = Modifier,
    state: FloatingMenuState?,
) {
    state?.let { floatingState ->
        var filterFabState by rememberSaveable {
            mutableStateOf(FilterFabState.COLLAPSED)
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
        ) {
            floatingState.items?.let { items ->

                FilterFabMenu(
                    items = items,
                    visible = filterFabState == FilterFabState.EXPANDED,
                )
            }

            FilterFab(
                state = filterFabState,
                icon = floatingState.icon,
                onClick = { state ->
                    floatingState.onMenuClick?.let {
                        it.invoke()
                    } ?: kotlin.run {
                        filterFabState = state
                    }
                },
            )
        }
    }
}

@Composable
fun FilterFab(
    modifier: Modifier = Modifier,
    state: FilterFabState,
    @DrawableRes icon: Int,
    onClick: (FilterFabState) -> Unit,
) {

    FloatingActionButton(
        modifier = modifier
            .semantics {
                contentDescription = MENU_FLOATING_BUTTON
            },
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
        onClick = {
            onClick(
                if (state == FilterFabState.EXPANDED) {
                    FilterFabState.COLLAPSED
                } else {
                    FilterFabState.EXPANDED
                },
            )
        },
        containerColor = colorScheme.onBackground,
        shape = CircleShape,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = colorScheme.primary,
        )
    }

}

enum class FilterFabState {
    EXPANDED,
    COLLAPSED
}

@Composable
fun FilterFabMenu(
    visible: Boolean,
    items: List<MenuItemState>,
    modifier: Modifier = Modifier,
) {

    val enterTransition = remember {
        expandVertically(
            expandFrom = Alignment.Bottom,
            animationSpec = tween(150, easing = FastOutSlowInEasing),
        ) + fadeIn(
            initialAlpha = 0.3f,
            animationSpec = tween(150, easing = FastOutSlowInEasing),
        )
    }

    val exitTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Bottom,
            animationSpec = tween(150, easing = FastOutSlowInEasing),
        ) + fadeOut(
            animationSpec = tween(150, easing = FastOutSlowInEasing),
        )
    }


    AnimatedVisibility(visible = visible, enter = enterTransition, exit = exitTransition) {
        Column(
            modifier = modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items.forEach { menuItem ->
                if (menuItem.isShown) {
                    MenuItem(
                        state = menuItem,
                    )
                }
            }
        }
    }
}

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    state: MenuItemState,
) {

    Row(
        modifier = modifier
            .clickable {
                state.onClick()
            },
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MenuLabel(label = state.label)
        MenuButton(item = state)
    }
}

@Composable
fun MenuButton(
    modifier: Modifier = Modifier,
    item: MenuItemState,
) {
    item.icon?.let { icon ->
        FloatingActionButton(
            modifier = modifier
                .size(32.dp)
                .clearAndSetSemantics {
                    contentDescription = item.semantics
                },
            shape = CircleShape,
            containerColor = colorScheme.onBackground,
            onClick = item.onClick,
        ) {
            Icon(
                modifier = Modifier
                    .size(16.dp)
                    .aspectRatio(1f),
                painter = painterResource(icon),
                contentDescription = null,
                tint = colorScheme.primary,
            )
        }
    }
}

@Composable
fun MenuLabel(
    @StringRes label: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                color = colorScheme.onBackground,
                shape = RoundedCornerShape(4.dp),
            ),
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp),
            text = stringResource(id = label),
            color = colorScheme.primary,
            style = typography.labelMedium,
            maxLines = 1,
        )
    }
}

data class FloatingMenuState(
    val icon: Int,
    val items: List<MenuItemState>? = null,
    val onMenuClick: (() -> Unit)? = null,
)

data class MenuItemState(
    val isShown: Boolean = true,
    @StringRes val label: Int,
    val icon: Int? = null,
    val onClick: () -> Unit,
    val semantics: String = "",
)

@Preview
@Composable
fun MenuPreview() {
    FamillySpandingsTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            FloatingActionMenu(
                state = FloatingMenuState(
                    icon = R.drawable.icon_edit,
                    items = listOf(
                        MenuItemState(
                            label = R.string.button_ok,
                            icon = R.drawable.icon_plus_outlined,
                            onClick = {},
                        ),
                        MenuItemState(
                            label = R.string.button_cancel,
                            icon = R.drawable.icon_delete,
                            onClick = {},
                        ),
                    ),
                ),
            )
        }
    }
}

package com.faigenbloom.famillyspandings.ui.statistics

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.faigenbloom.famillyspandings.comon.BaseDestination
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.statisticsPage(
    bottomNavigationOptions: (
        showNavigation: Boolean,
        index: Int,
    ) -> Unit,
) {
    composable(
        route = StatisticsRoute(),
    ) {
        bottomNavigationOptions(true, 3)
        val state by koinViewModel<StatisticsPageViewModel>().stateFlow.collectAsState()

        StatisticsPage(state)
    }
}

data object StatisticsRoute : BaseDestination(route = "StatisticsRoute") {
    operator fun invoke() = route
}

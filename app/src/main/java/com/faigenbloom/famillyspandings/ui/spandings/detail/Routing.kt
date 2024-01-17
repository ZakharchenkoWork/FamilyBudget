package com.faigenbloom.famillyspandings.ui.spandings.detail

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.faigenbloom.famillyspandings.comon.BaseDestination
import com.faigenbloom.famillyspandings.comon.DETAILS_LIST_ARG
import com.faigenbloom.famillyspandings.comon.DETAILS_LIST_KEY
import com.faigenbloom.famillyspandings.comon.fromJson
import com.faigenbloom.famillyspandings.comon.toJson
import com.faigenbloom.famillyspandings.ui.spandings.DetailUiData
import com.faigenbloom.famillyspandings.ui.spandings.SpendingDetailListWrapper
import org.koin.androidx.compose.koinViewModel


fun NavGraphBuilder.detailDialog(
    onSave: (updateDetails: SpendingDetailListWrapper) -> Unit,
    onBack: () -> Unit,
) {
    dialog(
        route = DetailDialogRoute.route,
        dialogProperties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
        arguments = listOf(
            navArgument(DETAILS_LIST_ARG) {
                type = NavType.StringType
            },
        ),
    ) {
        val viewModel = koinViewModel<DetailViewModel>()
        viewModel.onSave = onSave
        val state by viewModel.stateFlow.collectAsState()
        DetailDialog(
            state = state,
            onDismiss = onBack,
        )
    }
}

data object DetailDialogRoute : BaseDestination(
    route = "DetailDialogRoute/$DETAILS_LIST_KEY",
) {

    operator fun invoke(detailsList: List<DetailUiData>) =
        route.replace(
            DETAILS_LIST_KEY,
            SpendingDetailListWrapper(ArrayList(detailsList)).toJson(),
        )
}

internal class DetailsDialogArgs(val rawData: String? = null) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(savedStateHandle[DETAILS_LIST_ARG])

    fun getList(): ArrayList<DetailUiData> {
        return rawData?.fromJson<SpendingDetailListWrapper>()?.details ?: arrayListOf()
    }
}


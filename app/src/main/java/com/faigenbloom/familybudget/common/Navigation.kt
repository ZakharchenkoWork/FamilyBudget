package com.faigenbloom.familybudget.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavBackStackEntry

@Composable
fun <T : Any> NavBackStackEntry.getPoppedArgument(argumentKey: String, initial: T? = null): T? {
    val value = this.savedStateHandle.getStateFlow(argumentKey, initial).collectAsState().value
    this.savedStateHandle[argumentKey] = null
    return value
}

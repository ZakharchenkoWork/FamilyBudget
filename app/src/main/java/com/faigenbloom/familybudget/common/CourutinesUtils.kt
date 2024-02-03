package com.faigenbloom.familybudget.common

import kotlinx.coroutines.runBlocking
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun <T> executeSuspendable(lambda: suspend (callback: (T) -> Unit) -> Unit): T =
    suspendCoroutine { cont ->
        runBlocking {
            lambda { result ->
                cont.resume(result)
            }
        }
    }


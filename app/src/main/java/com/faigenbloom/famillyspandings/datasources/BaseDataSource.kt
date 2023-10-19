package com.faigenbloom.famillyspandings.datasources

interface BaseDataSource {
    suspend fun login(login: String, password: String): Boolean
}

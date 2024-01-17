package com.faigenbloom.famillyspandings.ui.login

import com.faigenbloom.famillyspandings.datasources.BaseDataSource

class LoginRepository(private val dataSource: BaseDataSource) {
    suspend fun login(login: String, password: String): Boolean {
        return dataSource.login(login, password)
    }
}

package com.aedo.my_heaven.model.datasource.put

import com.aedo.my_heaven.model.repository.DataRepository
import com.aedo.my_heaven.model.restapi.login.LoginSend
import  com.aedo.my_heaven.model.repository.Result

class PutLoginUseCase (private val repository: DataRepository) {
    suspend fun invoke(data: LoginSend): Result<LoginSend> =
        repository.putLogin1(data)
}
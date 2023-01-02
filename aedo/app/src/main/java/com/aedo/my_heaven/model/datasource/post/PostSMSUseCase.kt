package com.aedo.my_heaven.model.datasource.post

import com.aedo.my_heaven.model.repository.DataRepository
import com.aedo.my_heaven.model.restapi.login.LoginSMS
import  com.aedo.my_heaven.model.repository.Result


class PostSMSUseCase(private val repository: DataRepository) {
    suspend fun invoke(data: LoginSMS): Result<LoginSMS> =
        repository.postSMS1(data)
}
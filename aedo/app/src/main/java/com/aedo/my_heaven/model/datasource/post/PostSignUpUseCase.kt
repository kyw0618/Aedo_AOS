package com.aedo.my_heaven.model.datasource.post

import com.aedo.my_heaven.model.repository.DataRepository
import com.aedo.my_heaven.model.repository.Result
import com.aedo.my_heaven.model.restapi.login.LoginResult
import com.aedo.my_heaven.model.restapi.login.LoginSMS

class PostSignUpUseCase (private val repository: DataRepository) {
    suspend fun invoke(data: LoginResult): Result<LoginResult> =
        repository.postSignUp(data)
}
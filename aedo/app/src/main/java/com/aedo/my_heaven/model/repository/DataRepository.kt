package com.aedo.my_heaven.model.repository

import com.aedo.my_heaven.model.restapi.login.LoginSend
import  com.aedo.my_heaven.model.repository.Result
import com.aedo.my_heaven.model.restapi.login.LoginResult
import com.aedo.my_heaven.model.restapi.login.LoginSMS

interface DataRepository {

    suspend fun putLogin1(data: LoginSend): Result<LoginSend>

    suspend fun postSMS1(data: LoginSMS): Result<LoginSMS>
    suspend fun postSignUp(data: LoginResult): Result<LoginResult>

}
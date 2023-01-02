package com.aedo.my_heaven.model.repository

import com.aedo.my_heaven.api.APIService
import com.aedo.my_heaven.model.restapi.login.LoginResult
import com.aedo.my_heaven.model.restapi.login.LoginSMS
import com.aedo.my_heaven.model.restapi.login.LoginSend
import retrofit2.HttpException
import retrofit2.Response

class DataRepositoryImpl(private val service: APIService) : DataRepository {

    suspend fun <T : Any> handleApi(
        execute: suspend () -> Response<T>
    ): Result<T> {
        return try {
            val response = execute()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.Success(body)
            } else {
                Result.Error(code = response.code(), message = response.message())
            }
        } catch (e: HttpException) {
            Result.Error(code = e.code(), message = e.message())
        } catch (e: Throwable) {
            Result.Exception(e)
        }
    }

    override suspend fun putLogin1(data: LoginSend): Result<LoginSend> {
        return handleApi { service.putLogin1(data) }
    }


    override suspend fun postSMS1(data: LoginSMS): Result<LoginSMS> {
        return handleApi { service.postSMS1(data) }
    }
    override suspend fun postSignUp(data: LoginResult): Result<LoginResult> {
        return handleApi { service.postSignUp(data) }
    }

}
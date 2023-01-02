package com.aedo.my_heaven.model.repository

sealed class Result<T : Any> {
    class Success<T : Any>(val data: T) : Result<T>()
    class Error<T : Any>(val code: Int, val message: String?) : Result<T>()
    class Exception<T : Any>(val e: Throwable) : Result<T>()
}

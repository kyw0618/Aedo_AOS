package com.aedo.my_heaven.model.repository

interface BaseDataUseCase<T : Any> {
    suspend operator fun invoke(token: String): Result<T>
}
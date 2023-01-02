package com.aedo.my_heaven.di

import com.aedo.my_heaven.api.APIService
import com.aedo.my_heaven.api.RetrofitClient
import com.aedo.my_heaven.model.datasource.post.PostSMSUseCase
import com.aedo.my_heaven.model.datasource.post.PostSignUpUseCase
import com.aedo.my_heaven.model.datasource.put.PutLoginUseCase
import com.aedo.my_heaven.model.repository.DataRepository
import com.aedo.my_heaven.model.repository.DataRepositoryImpl
import com.aedo.my_heaven.util.`object`.Constant.BASE_URL
import com.aedo.my_heaven.util.base.MyApplication.Companion.prefs
import com.aedo.my_heaven.viewmodel.login.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
val modules = module {
    val myAccessToken = prefs.myaccesstoken ?: ""
    val newAccessToken = prefs.newaccesstoken ?: ""

    single<DataRepository> { DataRepositoryImpl(get()) }

    single<APIService> {
        RetrofitClient.getClient(BASE_URL).create(APIService::class.java)
    }

    viewModel { LoginViewModel(get(),get(),get()) }

    factory { PutLoginUseCase(get()) }

    factory { PostSMSUseCase(get()) }
    factory { PostSignUpUseCase(get()) }

}
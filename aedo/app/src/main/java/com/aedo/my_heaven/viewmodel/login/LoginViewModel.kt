package com.aedo.my_heaven.viewmodel.login

import android.util.Log
import android.util.LogPrinter
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aedo.my_heaven.model.datasource.post.PostSMSUseCase
import com.aedo.my_heaven.model.datasource.post.PostSignUpUseCase
import com.aedo.my_heaven.model.datasource.put.PutLoginUseCase
import com.aedo.my_heaven.model.restapi.login.LoginSMS
import com.aedo.my_heaven.util.base.BaseViewModel
import com.aedo.my_heaven.util.base.SingleLiveEvent
import com.aedo.my_heaven.util.log.LLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.aedo.my_heaven.model.repository.Result
import com.aedo.my_heaven.model.restapi.login.LoginResult
import com.aedo.my_heaven.model.restapi.login.LoginSend
import com.aedo.my_heaven.util.`object`.Constant.TAG
import com.aedo.my_heaven.util.base.MyApplication.Companion.prefs

class LoginViewModel(
    private val putLoginUseCase: PutLoginUseCase,
    private val postSMSUseCase: PostSMSUseCase,
    private val postSignUp: PostSignUpUseCase
) : BaseViewModel() {

    val mutableErrorMessage = SingleLiveEvent<String>()
    private var loginProcess = 0

    val phoneNum = MutableLiveData("")
    val authNum = MutableLiveData("")
    val birth = MutableLiveData("")
    val name = MutableLiveData("")

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        mutableErrorMessage.value = "Exception handled: ${throwable.localizedMessage}"
    }

    fun setPhoneNum(num: String) {
        phoneNum.postValue(num)
    }

    fun setAuthNum(num: String) {
        authNum.postValue(num)
    }

    fun setLoginProcess(loginProcess: Int) {
        this.loginProcess = loginProcess
    }

    /////////////////////API/////////////////////////////
    fun requestAuthSms() {
        Log.d(LLog.TAG,"로그인_문자메세지 API ${phoneNum.value}")
        val data = LoginSMS(phoneNum.value, authNum.value)
        CoroutineScope(Dispatchers.IO).launch {
            showProgress()
            when (val result = postSMSUseCase.invoke(data)) {
                is Result.Success -> {
                    viewEvent(SMS_SUCCESS)
                }
                is Result.Error -> {
                    viewEvent(SMS_ERROR)
                    Log.d(TAG,"문자인증 에러 -> ${result.message}")
                }
                is Result.Exception -> {
                    viewEvent(SMS_ERROR)
                    Log.d(TAG,"문자인증 실패 -> ${result.e}")
                }
            }
        }.also { hideProgress() }
    }

    fun authrequest() {
        val data = LoginSend(phoneNum.value, authNum.value)
        Log.d(LLog.TAG,"로그인_로그인 API $data")
        CoroutineScope(Dispatchers.IO).launch {
            showProgress()
            when (val result = putLoginUseCase.invoke(data)) {
                is Result.Success -> {
                    prefs.myaccesstoken = result.data.accesstoken.toString()
                    viewEvent(LOGIN_SUCCESS)
                }
                is Result.Error -> {
                    viewEvent(LOGIN_ERROR)
                    Log.d(TAG,"로그인 에러 -> ${result.message}")

                }
                is Result.Exception -> {
                    viewEvent(LOGIN_EXCEPTION)
                    Log.d(TAG,"로그인 실패 -> ${result.e}")
                }
            }
        }.also { hideProgress() }
    }

    fun signUp() {
        val data = LoginResult(
            phone = phoneNum.value,
            birth = birth.value,
            name = name.value,
            terms = "Y",
            smsnumber = authNum.value
        )
        LLog.e("회원가입 API")
        CoroutineScope(Dispatchers.IO).launch {
            when(val result = postSignUp.invoke(data)) {
                is Result.Success -> {
                    prefs.myaccesstoken = result.data.accesstoken.toString()
                    viewEvent(SIGH_UP_SUCCESS)
                }

                is Result.Error -> {
                    viewEvent(SIGH_UP_ERROR)
                    Log.d(TAG,"회원가입 에러 -> ${result.message}")

                }

                is Result.Exception -> {
                    viewEvent(SIGH_UP_ERROR)
                    Log.d(TAG,"회원가입 에러 -> ${result.e}")

                }
            }
        }
    }

    companion object {
        const val LOGIN_SUCCESS = 1111
        const val LOGIN_ERROR = 1112
        const val LOGIN_EXCEPTION = 1113

        const val SIGH_UP_SUCCESS = 2222
        const val SIGH_UP_ERROR = 2223

        const val SMS_SUCCESS = 3333
        const val SMS_ERROR = 3334
    }
}
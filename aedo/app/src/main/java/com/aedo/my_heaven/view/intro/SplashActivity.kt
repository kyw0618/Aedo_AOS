package com.aedo.my_heaven.view.intro

import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.aedo.my_heaven.R
import com.aedo.my_heaven.api.APIService
import com.aedo.my_heaven.api.ApiUtils
import com.aedo.my_heaven.databinding.ActivitySplashBinding
import com.aedo.my_heaven.model.restapi.base.*
import com.aedo.my_heaven.util.`object`.ActivityControlManager
import com.aedo.my_heaven.util.`object`.Constant
import com.aedo.my_heaven.util.`object`.Constant.RESULT_TRUE
import com.aedo.my_heaven.util.base.BaseActivity
import com.aedo.my_heaven.util.base.MyApplication
import com.aedo.my_heaven.util.log.LLog
import com.aedo.my_heaven.util.log.LLog.TAG
import com.aedo.my_heaven.util.network.ResultListener
import com.aedo.my_heaven.view.login.LoginActivity
import com.google.android.play.core.appupdate.AppUpdateManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var apiServices: APIService
    private var devpolicyversion: String? = null
    private var prefs = MyApplication.prefs
    private var appUpdate : AppUpdateManager?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        apiServices = ApiUtils.apiService

        inStatusBar()
        checkNetwork()
//        moveActivity()
    }

    // 네트워크 체크
    private fun checkNetwork() {
        LLog.e("1. 네트워크 확인")
        if(isInternetAvailable(this)) {
            requestPolicy()
        } else {
            networkDialog()
            return
        }
    }

//    // 4. 정책 버전 비교 및 저장
//    private fun checkPolicyVersion() {
//        LLog.e("4. 정책 버전 비교 및 저장")
//        requestPolicy(object : ResultListener {
//            override fun onSuccess() {
//                enablecheck()
//            }
//        })
//
//    }

//    private fun requestVerificationJson() {
//        LLog.e("검증 API")
//        val vercall: Call<Verification> = apiServices.getVerification("qwer")
//        vercall.enqueue(object : Callback<Verification> {
//            override fun onResponse(call: Call<Verification>, response: Response<Verification>) {
//                val result = response.body()
//                if (response.isSuccessful && result != null) {
//                    Log.d(TAG,"Vertification result SUCESS -> $result")
//                    checkPolicyVersion()
//                }
//                else {
//                    serverDialog()
//                }
//            }
//            override fun onFailure(call: Call<Verification>, t: Throwable) {
//                Log.d(TAG, "loadVerAPI error -> $t")
//                serverDialog()
//            }
//        })
//    }
//
    private fun requestPolicy() {
        LLog.e("정책 API")
        val vercall: Call<AppPolicy> = apiServices.getPolicy()
        vercall.enqueue(object : Callback<AppPolicy> {
            override fun onResponse(call: Call<AppPolicy>, response: Response<AppPolicy>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"Policy response SUCCESS -> $result")
                    realmPolicy(result)
                    requestLogin()
                }
                else {
                    Log.d(TAG,"Policy response ERROR -> $result")
                    serverDialog()
                }
            }
            override fun onFailure(call: Call<AppPolicy>, t: Throwable) {
                Log.d(TAG, "Policy error -> $t")
                serverDialog()
            }
        })
    }

    private fun realmPolicy(result: AppPolicy) {
        LLog.e("렐름")
        realm.executeTransaction {
            realm.where(Policy::class.java).findAll().deleteAllFromRealm()
            realm.where(Code::class.java).findAll().deleteAllFromRealm()
            realm.where(AppMenu::class.java).findAll().deleteAllFromRealm()
            realm.where(Coordinates::class.java).findAll().deleteAllFromRealm()

            realm.copyToRealm(result.policy!!)
            realm.copyToRealm(result.code!!)
            realm.copyToRealm(result.app_menu!!)
            realm.copyToRealm(result.coordinates!!)
        }
    }

    private fun requestLogin() {
        LLog.e("자동 로그인 API")
        val vercall: Call<AutoLogin> = apiServices.getautoLogin(prefs.myaccesstoken)
        vercall.enqueue(object : Callback<AutoLogin> {
            override fun onResponse(call: Call<AutoLogin>, response: Response<AutoLogin>) {
                val result = response.body()
                if (response.code() == 404 || response.code() == 401) {
                    prefs.newaccesstoken=result?.accesstoken
                    moveLogin()
                }
                else if(response.code() == 200){
                    getPreferences(0).edit().remove("PREF_ACCESS_TOKEN").apply()
                    prefs.newaccesstoken=result?.accesstoken
                    moveMain()
                    Toast.makeText(this@SplashActivity,"자동로그인이 되었습니다.",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<AutoLogin>, t: Throwable) {
                Log.d(TAG, "requestLogin error -> $t")
                serverDialog()
            }
        })
    }

    private fun enablecheck() {
        val useYn = realm.where(Policy::class.java).equalTo("id","APP_ENABLE_YN").findFirst()
        val popupText = realm.where(Policy::class.java).equalTo("id","APP_ENABLE_CONTENT").findFirst()
        if (useYn != null) {
            if (useYn.value.equals("Y")) {

                return
            }
        } else {

            return
        }
    }

    private fun moveLogin() {
        ActivityControlManager.delayRun({
            moveAndFinishActivity(LoginActivity::class.java) },
            Constant.SPLASH_WAIT
        )
    }
}
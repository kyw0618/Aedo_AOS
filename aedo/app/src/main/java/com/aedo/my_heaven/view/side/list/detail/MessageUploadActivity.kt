package com.aedo.my_heaven.view.side.list.detail

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import com.aedo.my_heaven.R
import com.aedo.my_heaven.api.APIService
import com.aedo.my_heaven.api.ApiUtils
import com.aedo.my_heaven.databinding.ActivityMessageUploadBinding
import com.aedo.my_heaven.model.restapi.base.*
import com.aedo.my_heaven.util.`object`.Constant
import com.aedo.my_heaven.util.base.BaseActivity
import com.aedo.my_heaven.util.base.MyApplication.Companion.prefs
import com.aedo.my_heaven.util.dialog.CustomDialogFragment
import com.aedo.my_heaven.util.log.LLog
import com.aedo.my_heaven.util.log.LLog.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class MessageUploadActivity : BaseActivity() {

    private lateinit var mBinding : ActivityMessageUploadBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waring)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_message_upload)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this

        inStatusBar()
        initTime()
    }

    private fun initTime() {
        val onlyDate: LocalDate = LocalDate.now()
        mBinding.tvMessageTimestamp.text = onlyDate.toString()
    }

    private fun condleAPI() {
        val id = intent.getStringExtra(Constant.MESSAGE_DETAIL_LLIST_ID)
        val title = mBinding.messageTitle.text.toString()
        val content = mBinding.messageDetailName.text.toString()
        val created = mBinding.tvMessageTimestamp.text.toString()
        val obId = id.toString()
        val data = CreateMessage(title, content, created, obId)

        LLog.e("조문메세지 작성_첫번째 API")
        apiServices.getCondole(prefs.myaccesstoken,data).enqueue(object : Callback<CreateMessage> {
            override fun onResponse(call: Call<CreateMessage>, response: Response<CreateMessage>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(TAG,"CreateMessage SUCCESS -> $result")
                    moveMessage()
                }
                else {
                    Log.d(TAG,"CreateMessage ERROR -> ${response.errorBody()}")
                    otherAPI()
                }
            }
            override fun onFailure(call: Call<CreateMessage>, t: Throwable) {
                Log.d(TAG,"CreateMessage FAIL -> $t")
            }
        })

    }

    private fun otherAPI() {
        val id = intent.getStringExtra(Constant.MESSAGE_DETAIL_LLIST_ID)
        val title = mBinding.messageTitle.text.toString()
        val content = mBinding.messageDetailName.text.toString()
        val created = mBinding.tvMessageTimestamp.text.toString()
        val obId = id.toString()
        val data = CreateMessage(title, content, created, obId)

        LLog.e("조문메세지 작성_두번째째 API")
       apiServices.getCondole(prefs.newaccesstoken,data).enqueue(object : Callback<CreateMessage> {
            override fun onResponse(call: Call<CreateMessage>, response: Response<CreateMessage>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(TAG,"CreateMessage id -> ${id.toString()}")
                    Log.d(TAG,"CreateMessage Second SUCCESS -> $result")
                    moveMessage()
                }
                else {
                    Log.d(TAG,"CreateMessage Second ERROR -> ${response.errorBody()}")
                }
            }
            override fun onFailure(call: Call<CreateMessage>, t: Throwable) {
                Log.d(TAG,"CreateMessage Second API FAIL -> $t")
            }
        })
    }

    fun onBackClick(v: View) {
        val dialog = CustomDialogFragment()
        val btn= arrayOf("계속","나가기") //여기에서 만약에 버튼이 하나라면 btn=arrayOf("취소")만 적으면 된다.
        dialog.arguments= bundleOf(
            "titleContext" to "기록을 중지하고 나갈까요?",
            "bodyContext" to "나가면 기록 중인 내용이 사라져요.",
            "btnData" to btn
        )
        dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener {
            override fun onClickNo() {
                //취소버튼을 눌렀을 때 처리할 곳
            }
            override fun onClickYes() {
                moveMessage()
            }
        })
        dialog.show(this.supportFragmentManager, "CustomDialog")
    }

    fun onMainClick(v: View) {
        moveMain()
    }

    fun onOkClick(v: View) {
        val title = mBinding.messageTitle.text.toString()
        val obld = mBinding.messageDetailName.text.toString()
        when {
            title.isEmpty() -> {
                mBinding.messageTitle.error = "미입력"
                Toast.makeText(this,"메세지를 입력해 주세요", Toast.LENGTH_SHORT).show().toString()
            }
            obld.isEmpty() -> {
                mBinding.messageDetailName.error = "미입력"
                Toast.makeText(this,"성함을 입력해 주세요", Toast.LENGTH_SHORT).show().toString()
            }
            else -> {
                val dlg: AlertDialog.Builder = AlertDialog.Builder(
                    this,
                    android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
                )
                dlg.setTitle("조문메세지 등록") //제목
                dlg.setMessage("조문메세지를 등록 하시겠습니까?")
                dlg.setPositiveButton("확인") { dialog, which ->
                    condleAPI()
                    dialog.dismiss()
                }
                dlg.setNegativeButton("취소") { dialog, which ->
                    dialog.dismiss()
                }
                dlg.show()
            }

        }
    }

    override fun onBackPressed() {
        val dialog = CustomDialogFragment()
        val btn= arrayOf("계속","나가기") //여기에서 만약에 버튼이 하나라면 btn=arrayOf("취소")만 적으면 된다.
        dialog.arguments= bundleOf(
            "titleContext" to "기록을 중지하고 나갈까요?",
            "bodyContext" to "나가면 기록 중인 내용이 사라져요.",
            "btnData" to btn
        )
        dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener {
            override fun onClickNo() {
                //취소버튼을 눌렀을 때 처리할 곳
            }
            override fun onClickYes() {
                moveMessage()
            }
        })
        dialog.show(this.supportFragmentManager, "CustomDialog")
    }
}
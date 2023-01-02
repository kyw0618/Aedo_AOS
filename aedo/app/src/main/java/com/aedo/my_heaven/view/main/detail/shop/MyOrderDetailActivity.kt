package com.aedo.my_heaven.view.main.detail.shop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.aedo.my_heaven.R
import com.aedo.my_heaven.adapter.MyOrderAdapter
import com.aedo.my_heaven.api.APIService
import com.aedo.my_heaven.api.ApiUtils
import com.aedo.my_heaven.databinding.ActivityMyOrderDetailBinding
import com.aedo.my_heaven.model.shop.PostOrder
import com.aedo.my_heaven.util.`object`.Constant.MY_ORDER_COMPANY
import com.aedo.my_heaven.util.`object`.Constant.MY_ORDER_CREATED
import com.aedo.my_heaven.util.`object`.Constant.MY_ORDER_ID
import com.aedo.my_heaven.util.`object`.Constant.MY_ORDER_ITEM
import com.aedo.my_heaven.util.`object`.Constant.MY_ORDER_MERCHANT_UID
import com.aedo.my_heaven.util.`object`.Constant.MY_ORDER_ORDER_COMPLETE
import com.aedo.my_heaven.util.`object`.Constant.MY_ORDER_PLACE_NAME
import com.aedo.my_heaven.util.`object`.Constant.MY_ORDER_PRICE
import com.aedo.my_heaven.util.`object`.Constant.MY_ORDER_RECEIVER_NAME
import com.aedo.my_heaven.util.`object`.Constant.MY_ORDER_RECEIVER_PHONE
import com.aedo.my_heaven.util.`object`.Constant.MY_ORDER_SENDER_NAME
import com.aedo.my_heaven.util.`object`.Constant.MY_ORDER_SENDER_PHONE
import com.aedo.my_heaven.util.`object`.Constant.MY_ORDER_WORD
import com.aedo.my_heaven.util.`object`.Constant.TAG
import com.aedo.my_heaven.util.base.BaseActivity
import com.aedo.my_heaven.util.base.MyApplication.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyOrderDetailActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMyOrderDetailBinding
    private lateinit var apiServices: APIService
    private var readapter: MyOrderAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_order_detail)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        inStatusBar()
        initView()
    }

    private fun initView() {
        val place = intent.getStringExtra(MY_ORDER_PLACE_NAME)
        val item = intent.getStringExtra(MY_ORDER_ITEM)
        val price = intent.getStringExtra(MY_ORDER_PRICE)
        val receiver_name = intent.getStringExtra(MY_ORDER_RECEIVER_NAME)
        val receiver_number = intent.getStringExtra(MY_ORDER_RECEIVER_PHONE)
        val sender_name = intent.getStringExtra(MY_ORDER_SENDER_NAME)
        val sender_number = intent.getStringExtra(MY_ORDER_SENDER_PHONE)
        val word = intent.getStringExtra(MY_ORDER_WORD)
        val company = intent.getStringExtra(MY_ORDER_COMPANY)
        val created = intent.getStringExtra(MY_ORDER_CREATED)
        val order_complete = intent.getStringExtra(MY_ORDER_ORDER_COMPLETE)
        val merchant_uid = intent.getStringExtra(MY_ORDER_MERCHANT_UID)

        mBinding.tvMyorderPlace.text = place.toString()
        mBinding.myorderFlower.text = item.toString()
        mBinding.tvMyorderPrice.text = price.toString()
        mBinding.tvMyorderReceiverName.text = receiver_name.toString()
        mBinding.tvMyorderReceiverPhone.text = receiver_number.toString()
        mBinding.tvMyorderSendName.text = sender_name.toString()
        mBinding.tvMyorderSendPhone.text = sender_number.toString()
        mBinding.tvMyorderWord.text = word.toString()
        mBinding.tvMyorderCompany.text = company.toString()
        mBinding.tvMyorderCreate.text = created.toString()
        mBinding.tvMyorderWordStright.text = merchant_uid.toString()
        if(order_complete?.equals("")!!) {
            mBinding.tvOrderComplete.text = "주문 대기"
        }
    }

    fun onBackClick(v: View) {
        moveMyOrder()
    }

    fun onOrderComplete(v: View) {
        Log.d(TAG,"주문 완료")
        val id = intent.getStringExtra(MY_ORDER_ID)
        val complete = "true".toBoolean()
        apiServices.putOrder(prefs.newaccesstoken,id,complete).enqueue(object :
            Callback<PostOrder> {
            override fun onResponse(call: Call<PostOrder>, response: Response<PostOrder>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    moveMyOrder()
                }
                else {
                    Log.d(TAG,"getCreate Second API ERROR -> ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<PostOrder>, t: Throwable) {
                Log.d(TAG,"getCreate Second Fail -> $t")
                serverDialog()
            }
        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MyOrderActivity::class.java))
        finish()
    }
}
package com.aedo.my_heaven.view.main.detail.shop

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aedo.my_heaven.R
import com.aedo.my_heaven.adapter.MyOrderAdapter
import com.aedo.my_heaven.adapter.OrderFragmentAdapter
import com.aedo.my_heaven.adapter.RecyclerAdapter
import com.aedo.my_heaven.api.APIService
import com.aedo.my_heaven.api.ApiUtils
import com.aedo.my_heaven.databinding.ActivityMyOrderBinding
import com.aedo.my_heaven.model.shop.MyOrder
import com.aedo.my_heaven.model.shop.MyOrders
import com.aedo.my_heaven.util.alert.LoadingDialog
import com.aedo.my_heaven.util.base.BaseActivity
import com.aedo.my_heaven.util.base.MyApplication
import com.aedo.my_heaven.util.base.MyApplication.Companion.prefs
import com.aedo.my_heaven.util.log.LLog
import com.aedo.my_heaven.view.main.MainActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.kakao.sdk.common.KakaoSdk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyOrderActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMyOrderBinding
    private lateinit var apiServices: APIService
    private val tabTitleArray = arrayOf(
        "주문대기",
        "주문완료",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_order)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        dialog = LoadingDialog(this)
        inStatusBar()
        initView()

    }


    private fun initView() {
        val viewPager = mBinding.vpSearch
        val tabLayout = mBinding.tlSearch

        viewPager.adapter = OrderFragmentAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()
    }

    fun onBackClick(v: View) {
        moveMain()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun onMainClick(v: View) {
        moveMain()
    }
}
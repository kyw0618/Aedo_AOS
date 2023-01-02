package com.aedo.my_heaven.view.main.detail.shop.order

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.aedo.my_heaven.R
import com.aedo.my_heaven.adapter.MyOrderAdapter
import com.aedo.my_heaven.api.APIService
import com.aedo.my_heaven.api.ApiUtils
import com.aedo.my_heaven.databinding.FragmentBeforeOrderBinding
import com.aedo.my_heaven.model.shop.MyOrder
import com.aedo.my_heaven.model.shop.MyOrders
import com.aedo.my_heaven.util.`object`.Constant.TAG
import com.aedo.my_heaven.util.base.MyApplication
import com.aedo.my_heaven.util.base.MyApplication.Companion.prefs
import com.aedo.my_heaven.util.log.LLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BeforeOrderFragment : Fragment() {

    private lateinit var mBinding : FragmentBeforeOrderBinding
    private lateinit var apiServices: APIService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_before_order, container, false)
        apiServices = ApiUtils.apiService
        mBinding.fragment = this
        initView()
        return mBinding.root
    }

    private fun initView() {
        val vercall: Call<MyOrder> = apiServices.getMyOrder(MyApplication.prefs.newaccesstoken)
        vercall.enqueue(object : Callback<MyOrder> {
            override fun onResponse(call: Call<MyOrder>, response: Response<MyOrder>) {
                val result = response.body()
                val complete_boolean = false
                if (response.isSuccessful && result != null) {
                    val complete = result.result?.map { it.order_complete }
                    if(result.result!!.isEmpty()) {
                        mBinding.recyclerViewOrder.visibility = View.GONE
                        mBinding.tvOrderNothing.visibility = View.VISIBLE
                    } else {
                        val complete = result.result.map { it.order_complete }
                        if (complete.equals(complete_boolean)) {
                            mBinding.tvOrderNothing.visibility = View.VISIBLE

                        } else {
                            mBinding.recyclerViewOrder.visibility = View.VISIBLE
                            mBinding.tvOrderNothing.visibility = View.GONE
                            setAdapter(result.result)
                        }
                    }
                } else {
                    Log.d(LLog.TAG,"List Second response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<MyOrder>, t: Throwable) {
                Log.d(LLog.TAG, "List Second Fail -> $t")
            }
        })
    }

    private fun setAdapter(order: List<MyOrders>?) {
        val adapter = MyOrderAdapter(order!!, context!!)
        val rv = mBinding.recyclerViewOrder
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context)
        rv.setHasFixedSize(true)
    }
}
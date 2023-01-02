package com.aedo.my_heaven.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aedo.my_heaven.R
import com.aedo.my_heaven.model.shop.MyOrders
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
import com.aedo.my_heaven.view.main.detail.shop.MyOrderDetailActivity

class MyOrderAdapter (private val postList : List<MyOrders>, val context : Context)
    : RecyclerView.Adapter<MyOrderAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(postList[position],context)
    }

    override fun getItemCount(): Int {
        return postList.count()
    }

    inner class ViewHolder (itemView: View? ) : RecyclerView.ViewHolder(itemView!!){
        val cl_body = itemView?.findViewById<ConstraintLayout>(R.id.cl_order)
        val order_flower = itemView?.findViewById<TextView>(R.id.tv_flower_pay)
        val myorder_receiver_name = itemView?.findViewById<TextView>(R.id.tv_receiver_name_detail)
        val myorder_send_name = itemView?.findViewById<TextView>(R.id.tv_send_name_detail)
        val order_timestamp = itemView?.findViewById<TextView>(R.id.tv_order_timestamp)

        fun bind(itemPhoto : MyOrders?, context: Context){
            order_flower?.text = "${itemPhoto?.item.toString()}/${itemPhoto?.price.toString()}"
            myorder_receiver_name?.text = itemPhoto?.receiver_name.toString()
            myorder_send_name?.text = itemPhoto?.sender_name.toString()
            order_timestamp?.text = itemPhoto?.created.toString()

            cl_body?.setOnClickListener {
                val intent = Intent(context, MyOrderDetailActivity::class.java)
                intent.putExtra(MY_ORDER_PLACE_NAME, itemPhoto?.place.toString())
                intent.putExtra(MY_ORDER_ITEM, itemPhoto?.item.toString())
                intent.putExtra(MY_ORDER_PRICE, itemPhoto?.price.toString())
                intent.putExtra(MY_ORDER_RECEIVER_NAME, itemPhoto?.receiver_name.toString())
                intent.putExtra(MY_ORDER_RECEIVER_PHONE, itemPhoto?.receiver_number.toString())
                intent.putExtra(MY_ORDER_SENDER_NAME, itemPhoto?.sender_name.toString())
                intent.putExtra(MY_ORDER_SENDER_PHONE, itemPhoto?.sender_number.toString())
                intent.putExtra(MY_ORDER_WORD, itemPhoto?.word.toString())
                intent.putExtra(MY_ORDER_COMPANY, itemPhoto?.company.toString())
                intent.putExtra(MY_ORDER_CREATED, itemPhoto?.created.toString())
                intent.putExtra(MY_ORDER_ORDER_COMPLETE, itemPhoto?.order_complete.toString())
                intent.putExtra(MY_ORDER_MERCHANT_UID, itemPhoto?.merchant_uid.toString())
                intent.putExtra(MY_ORDER_ID, itemPhoto?.id.toString())
                ContextCompat.startActivity(itemView.context, intent, null)
            }
        }
    }
}
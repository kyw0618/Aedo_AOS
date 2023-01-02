package com.aedo.my_heaven.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aedo.my_heaven.view.main.detail.shop.order.AfterOrderFragment
import com.aedo.my_heaven.view.main.detail.shop.order.BeforeOrderFragment

private const val NUM_TABS = 2

class OrderFragmentAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return BeforeOrderFragment()
            1 -> return AfterOrderFragment()
        }
        return BeforeOrderFragment()
    }
}
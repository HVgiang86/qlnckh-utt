package com.example.mvvm.views.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeViewPagerAdapter(
    fragment: Fragment,
    private val fragments: MutableList<Fragment>,
) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}

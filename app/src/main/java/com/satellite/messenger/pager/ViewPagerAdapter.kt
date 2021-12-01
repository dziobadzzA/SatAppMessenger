package com.satellite.messenger.pager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.satellite.messenger.pager.fragment.DataItem
import com.satellite.messenger.pager.fragment.image.ImagePager
import com.satellite.messenger.pager.fragment.info.InfoPager
import com.satellite.messenger.pager.fragment.text.TextPager
import com.satellite.messenger.pager.fragment.video.VideoPager
import com.satellite.messenger.utils.TAB_TITLES

class ViewPagerAdapter(fa: Fragment, private val dataItem: DataItem): FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = TAB_TITLES.size

    override fun createFragment(position: Int): Fragment {
       return replaceFragment(position)
    }

    private fun replaceFragment(position: Int):Fragment {
        return when(position) {
            4 -> InfoPager(dataItem)
            3 -> VideoPager(dataItem)
            2 -> ImagePager(dataItem)
            else -> {

                var str = ""
                when(position) {
                    0 -> str = "TV"
                    1 -> str = "beacon"
                }
                return TextPager(dataItem, str)
            }
        }
    }
}
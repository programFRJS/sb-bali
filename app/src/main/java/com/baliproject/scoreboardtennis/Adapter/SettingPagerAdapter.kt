package com.baliproject.scoreboardtennis.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.baliproject.scoreboardtennis.UI.Fragment.FragmentDua
import com.baliproject.scoreboardtennis.UI.Fragment.FragmentSatu
import com.baliproject.scoreboardtennis.UI.Fragment.FragmentTiga

class SettingPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentSatu()
            1 -> FragmentDua()
            2 -> FragmentTiga()
            else -> FragmentSatu()
        }
    }
}

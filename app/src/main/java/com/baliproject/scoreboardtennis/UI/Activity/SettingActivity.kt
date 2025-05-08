package com.baliproject.scoreboardtennis.UI.Activity

import com.baliproject.scoreboardtennis.Database.AppDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.baliproject.scoreboardtennis.Dao.IpAddressDao
import com.baliproject.scoreboardtennis.R
import com.baliproject.scoreboardtennis.Adapter.SettingPagerAdapter
import com.baliproject.scoreboardtennis.databinding.ActivitySettingBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var db: AppDatabase
    private lateinit var dao: IpAddressDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        val adapter = SettingPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Setting Wifi"
                1 -> "WiFi Information"
                2 -> "Brightness"
                else -> "Tab ${position + 1}"
            }
        }.attach()
    }
}

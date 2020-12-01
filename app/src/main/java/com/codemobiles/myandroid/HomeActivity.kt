package com.codemobiles.myandroid

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.codemobiles.myandroid.databinding.ActivityHomeBinding
import com.codemobiles.myandroid.databinding.ActivityMainBinding
import com.codemobiles.myandroid.databinding.CustomTabBinding
import com.codemobiles.myandroid.ui.main.SectionsPagerAdapter
//import com.codemobiles.myandroid.ui.main.TAB_ICONS
//import com.codemobiles.myandroid.ui.main.TAB_TITLES
import com.codemobiles.myandroid.utilities.HorizontalFlipTransformation
import com.codemobiles.myandroid.utilities.PREFS_TOKEN
import com.google.android.material.tabs.TabLayoutMediator
import com.pixplicity.easyprefs.library.Prefs

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTabs()
        setupEventWidget()
    }

    private fun setupTabs() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, lifecycle)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.viewPager.setPageTransformer(HorizontalFlipTransformation())
//        binding.viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if(position == 0){
                    binding.fab.visibility = View.VISIBLE
                }else{
                    binding.fab.visibility = View.INVISIBLE
                }
            }
        })

        TabLayoutMediator(
            binding.tabs, binding.viewPager
        ) { tab, position ->

            val binding = CustomTabBinding.inflate(layoutInflater)

            binding.textTab.text = SectionsPagerAdapter.TAB_TITLES[position]
            binding.iconTab.setImageResource(SectionsPagerAdapter.TAB_ICONS[position])

            tab.customView = binding.root
        }.attach()

    }

    private fun setupEventWidget() {
        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
            logout()
        }
    }


    private fun logout() {
//        Prefs.clear()
        Prefs.remove(PREFS_TOKEN)

        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
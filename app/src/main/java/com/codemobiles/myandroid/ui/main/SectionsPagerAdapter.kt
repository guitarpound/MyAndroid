package com.codemobiles.myandroid.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.codemobiles.myandroid.ChartFragment
import com.codemobiles.myandroid.JSONFragment
import com.codemobiles.myandroid.R

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(
    private val context: Context,
    fm: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount() = TAB_TITLES.size

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {
                JSONFragment.newInstance()
            }
            1 -> {
                ChartFragment.newInstance()
            }
            else -> {
                JSONFragment.newInstance()
            }
        }
    }
}
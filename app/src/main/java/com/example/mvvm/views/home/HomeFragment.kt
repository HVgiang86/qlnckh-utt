package com.example.mvvm.views.home

import android.view.LayoutInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.mvvm.R
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.databinding.FragmentHomeBinding
import com.example.mvvm.views.history.HistoryFragment
import com.example.mvvm.views.incharge.InChargeFragment
import com.example.mvvm.views.profile.ProfileFragment
import com.example.mvvm.views.projectlist.ProjectListFragment
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override val viewModel: HomeViewModel by viewModels()

    private val inChargeFragment by lazy { InChargeFragment.newInstance() }
    private val projectListFragment by lazy { ProjectListFragment.newInstance() }
    private val historyFragment by lazy { HistoryFragment.newInstance() }
    private val profileFragment by lazy { ProfileFragment.newInstance() }

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
    }

    override fun initialize() {
        setUpBottomAppBar()

        val fragments = mutableListOf<Fragment>(projectListFragment, inChargeFragment, historyFragment, profileFragment)

        viewBinding.apply {
            homeViewPaper.apply {
                registerOnPageChangeCallback(onPageChangeCallBack)
                isUserInputEnabled = false
                offscreenPageLimit = OFF_SCREEN_PAGE_LIMIT
                adapter = fragments.let { HomeViewPagerAdapter(this@HomeFragment, it) }
                val currentItem = POSITION_IN_CHARGE_NAVIGATE
                setCurrentItem(currentItem, false)
            }

            homeBottomNavigation.apply {
                setOnItemSelectedListener { onNavigationItemSelected(it) }
                menu.findItem(R.id.inCharge).isChecked = true
            }
        }
    }

    private fun onNavigationItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.projectList -> {
            viewBinding.homeViewPaper.currentItem = POSITION_PROJECT_LIST_NAVIGATE
            true
        }

        R.id.inCharge -> {
            viewBinding.homeViewPaper.currentItem = POSITION_IN_CHARGE_NAVIGATE
            true
        }

        R.id.history -> {
            viewBinding.homeViewPaper.currentItem = POSITION_HISTORY_NAVIGATE
            true
        }

        R.id.profile -> {
            viewBinding.homeViewPaper.currentItem = POSITION_PROFILE_NAVIGATE
            true
        }

        else -> false
    }

    private val onPageChangeCallBack = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            when (position) {
                POSITION_PROJECT_LIST_NAVIGATE -> {
                    viewBinding.homeBottomNavigation.menu.findItem(R.id.projectList).isChecked = true
                }

                POSITION_IN_CHARGE_NAVIGATE -> {
                    viewBinding.homeBottomNavigation.menu.findItem(R.id.inCharge).isChecked = true
                }

                POSITION_HISTORY_NAVIGATE -> {
                    viewBinding.homeBottomNavigation.menu.findItem(R.id.history).isChecked = true
                }

                else -> {
                    viewBinding.homeBottomNavigation.menu.findItem(R.id.profile).isChecked = true
                }
            }
        }
    }

    private fun setUpBottomAppBar() {
        val radius = resources.getDimension(R.dimen.dp_16)
        val bottomBarBackground = viewBinding.bottomAppbar.background as? MaterialShapeDrawable ?: return
        bottomBarBackground.shapeAppearanceModel =
            bottomBarBackground.shapeAppearanceModel.toBuilder().setTopRightCorner(CornerFamily.ROUNDED, radius).setTopLeftCorner(CornerFamily.ROUNDED, radius)
                .build()
    }

    override fun onDestroyView() {
        viewBinding.homeViewPaper.unregisterOnPageChangeCallback(onPageChangeCallBack)
        super.onDestroyView()
    }

    companion object {
        const val EXACT_ALARM_ALLOW = 3
        const val POSITION_PROJECT_LIST_NAVIGATE = 0
        const val POSITION_IN_CHARGE_NAVIGATE = 1
        const val POSITION_HISTORY_NAVIGATE = 2
        const val POSITION_PROFILE_NAVIGATE = 3
        const val OFF_SCREEN_PAGE_LIMIT = 4

        fun newInstance() = HomeFragment()
    }
}

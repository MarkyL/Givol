package com.givol.activities

import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.givol.R
import com.givol.core.GivolActivity
import com.givol.core.SupportsOnBackPressed
import com.givol.screens.SignInScreen
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.view.*
import timber.log.Timber

class MainActivity : GivolActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeDrawer()
        
        navigator.replace(SignInScreen())
    }

    override fun onResume() {
        super.onResume()
        navigator.takeActivity(this)

    }

    override fun onPause() {
        super.onPause()
        navigator.dropActivity()
    }

    //region drawer
    private fun initializeDrawer() {
        addDrawerListener()
    }

    private fun addDrawerListener() {
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {
                Timber.i("$TAG -  onDrawerOpened")
            }

            override fun onDrawerClosed(drawerView: View) {

            }

            override fun onDrawerStateChanged(newState: Int) {

            }
        })

        navigationView.setNavigationItemSelectedListener { item ->
            Timber.i("$TAG -  onNavigationItemSelected - ${item.title}")

            when (item.itemId) {
                R.id.navDepartmentInfo -> {
                    //TODO("onNavigationItemSelected")
                }
            }
            drawerLayout.closeDrawers()

            true
        }
    }

    fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    fun setDrawerTitle(title: String) {
        navigationView.getHeaderView(0).drawerTitle.text = title
    }

    //endregion

    override fun onBackPressed() {
        super.onBackPressed()
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (fragment != null && fragment is SupportsOnBackPressed) {
            if ((fragment as SupportsOnBackPressed).onBackPressed()) {
                return
            }
        }
        super.onBackPressed()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
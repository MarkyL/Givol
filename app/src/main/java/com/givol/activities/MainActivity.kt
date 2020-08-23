package com.givol.activities

import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.givol.R
import com.givol.core.GivolActivity
import com.givol.core.GivolFragment
import com.givol.core.SupportsOnBackPressed
import com.givol.navigation.arguments.TransferInfo
import com.givol.screens.MainScreen
import com.givol.screens.SignInScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.view.*
import timber.log.Timber

class MainActivity : GivolActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeDrawer()

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        currentUser?.let {
            // user is already logged in.
            val transferInfo = TransferInfo()
            transferInfo.uid = it.uid
            navigator.replace(MainScreen(transferInfo))
        } ?: run {
            val transferInfo = TransferInfo()
            transferInfo.flow = TransferInfo.Flow.SignIn
            navigator.replace(SignInScreen(transferInfo))
        }
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
                R.id.navActiveContest -> {
                    //TODO("onNavigationItemSelected")
                }
            }
            drawerLayout.closeDrawers()

            true
        }
    }

    fun setDrawerState(currentFragment: GivolFragment) {
        val drawerEnabled: Boolean = currentFragment.isDrawerEnabled
        if (!drawerEnabled) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        drawerLayout.isEnabled = drawerEnabled
        drawerLayout.setDrawerLockMode(if (drawerEnabled) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
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

//        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
//        if (currentFragment != null) {
//            setDrawerState(currentFragment as GivolFragment)
//        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
package com.givol.activities

import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.givol.R
import com.givol.core.GivolActivity
import com.givol.core.GivolFragment
import com.givol.core.SupportsOnBackPressed
import com.givol.managers.UserFirebaseManager
import com.givol.navigation.arguments.TransferInfo
import com.givol.screens.FinishedContestsScreen
import com.givol.screens.MainScreen
import com.givol.screens.SignInScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.view.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : GivolActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var uid: String

    private val userManager: UserFirebaseManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeDrawer()

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (savedInstanceState == null) {
            updateUI(currentUser)
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        currentUser?.let {
            // user is already logged in.
            this.uid = it.uid
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
                R.id.navFinishedContest -> {
                    val transferInfo = TransferInfo()
                    transferInfo.uid = this.uid
                    navigator.replace(FinishedContestsScreen(transferInfo))
                }
                R.id.navTest -> {
                    makeMockTestForFinishedContest()
                }
            }
            drawerLayout.closeDrawers()

            true
        }
    }

    private fun makeMockTestForFinishedContest() {
        userManager.addSingleContestListener(uid)
        userManager.userLiveData.observe(this, Observer { user ->
            user.contests.active.entries.forEach {
                // let's just move first to finish.
                it.value.contestState = "WIN"
                it.value.used = false
                userManager.moveActiveToFinished(uid, it.value)
            }
        })
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
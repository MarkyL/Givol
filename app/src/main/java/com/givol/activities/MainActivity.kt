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
import com.givol.dialogs.AbstractDialog
import com.givol.dialogs.GivolDialog
import com.givol.fragments.main.MainViewModel
import com.givol.managers.ContestsFirebaseManager
import com.givol.managers.UserFirebaseManager
import com.givol.model.CONTEST_WIN_STATE
import com.givol.model.FBContest
import com.givol.navigation.arguments.TransferInfo
import com.givol.screens.FinishedContestsScreen
import com.givol.screens.MainScreen
import com.givol.screens.SignInScreen
import com.givol.utils.DateTimeHelper
import com.givol.utils.observeOnce
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

class MainActivity : GivolActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var uid: String

    private val userManager: UserFirebaseManager by inject()
    private val contestManager: ContestsFirebaseManager by inject()
    private val viewModel by viewModel<MainViewModel>()

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

            setDrawerButtons(it)
        } ?: run {
            val transferInfo = TransferInfo()
            transferInfo.flow = TransferInfo.Flow.SignIn
            navigator.replace(SignInScreen(transferInfo))
        }
    }

    private fun setDrawerButtons(user: FirebaseUser): Unit {
        if (!user.email.isNullOrEmpty() && user.email == "eli@givol.com") {
            navigationView.menu.findItem(R.id.navRunContests).isVisible = true
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
                R.id.navRunContests -> {
                    runContests()
                }
            }
            drawerLayout.closeDrawers()

            true
        }
    }

    //region contest run logic
    private fun runContests() {
        viewModel.getContests().observeOnce(this, Observer<List<FBContest>> { activeContests ->
            //Timber.i("runContests = $activeContests")
            val contestsIDsList = mutableListOf<String>()
            activeContests.forEach { currentActiveContest ->
                if (hasContestMetRequirementsToStart(currentActiveContest)) {
                    Timber.i("runContests: A contest with ID [${currentActiveContest.contestID}] has met requirements to start ")
                    val winnerID = randomizeWinner(currentActiveContest)
                    for ((participantID, userContest) in currentActiveContest.participantsMap) {
                        // need to update their state in the /contests/active tree
                        // need to update their state in the /users/ID/ tree
                        val userContestState =
                            if (participantID == winnerID) CONTEST_WIN_STATE.WIN else CONTEST_WIN_STATE.CONSOLATION
                        userContest.contestState = userContestState.name
                        currentActiveContest.contestState = userContestState.name
                        userManager.moveActiveToFinished(participantID, currentActiveContest)
                    }
                    contestManager.moveActiveToFinished(currentActiveContest)
                    contestsIDsList.add(currentActiveContest.contestID)
                }
            }
            showFinishedContestRunningDialog(contestsIDsList)
        })
    }

    private fun showFinishedContestRunningDialog(contestsIDsList: MutableList<String>) {
        Timber.i("showFinishedContestRunningDialog: $contestsIDsList")
        val dialog = GivolDialog(
            title = resources.getString(R.string.finished_run_contest_title),
            subtitle = resources.getString(R.string.finished_run_contest_subtitle, contestsIDsList),
            positiveButtonText = R.string.finished_run_contest_positive,
            iconDrawable = R.drawable.ic_baseline_check_24,
            callback = object : AbstractDialog.Callback {
                override fun onDialogPositiveAction(requestCode: Int) {
                    Timber.i("showFinishedContestRunningDialog positive clicked")
                }

                override fun onDialogNegativeAction(requestCode: Int) {
                    Timber.i("onDialogNegativeAction")
                }
            })

        dialog.show(supportFragmentManager, GivolDialog.TAG)
    }

    private fun hasContestMetRequirementsToStart(contest: FBContest): Boolean {
        val endTime = DateTimeHelper.getDateFormat(contest.times.dateEndStr)
        return if (endTime != null && endTime.before(Date())) {
            Timber.i("hasContestMetRequirementsToStart - contest[${contest.contestID}]: endTime is *before* now")
            //TODO: ***Fix the condition here, it's <= for TESTING ONLY!!! has to be >=***
            if (contest.participantsMap.size >= contest.minParticipants) {
                Timber.i("hasContestMetRequirementsToStart - contest[${contest.contestID}]: met min participant threshold: \n required: ${contest.minParticipants} | registered = ${contest.participantsMap.size}")
                true
            } else {
                Timber.i("hasContestMetRequirementsToStart - contest[${contest.contestID}]: contest hasn't met min participant threshold.")
                false
            }
        } else {
            Timber.i("hasContestMetRequirementsToStart - contest[${contest.contestID}]: endTime is *after* now")
            false
        }
    }

    private fun randomizeWinner(contest: FBContest): String {
        val winnerIndex = Random().nextInt(contest.participantsMap.size)
        val winnerKey = contest.participantsMap.keys.elementAt(winnerIndex)
        Timber.i("randomizeWinner - contest[${contest.contestID}]: winnerIndex = $winnerIndex, winnerKey = $winnerKey")
        return winnerKey
    }

    //endregion


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
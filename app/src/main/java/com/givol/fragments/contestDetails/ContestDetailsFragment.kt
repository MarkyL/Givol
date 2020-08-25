package com.givol.fragments.contestDetails

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.givol.R
import com.givol.adapters.PicturesAdapter
import com.givol.core.Action
import com.givol.core.GivolFragment
import com.givol.core.SupportsOnBackPressed
import com.givol.dialogs.GivolDialog
import com.givol.fragments.main.MainViewModel
import com.givol.model.FBContest
import com.givol.model.FBUser
import com.givol.model.User.Companion.MAX_CONTESTS_REGISTRATION
import com.givol.navigation.arguments.TransferInfo
import com.givol.screens.BusinessDetailsScreen
import com.givol.screens.ContestDetailsScreen
import com.givol.utils.DateTimeHelper
import com.givol.utils.GlideApp
import com.givol.widgets.GivolToolbar
import kotlinx.android.synthetic.main.fragment_contest_details.*
import kotlinx.android.synthetic.main.givol_toolbar.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

//TODO: viewpager with pictures of the business/contest/promotion
//TODO: toolbar title with business name
//TODO: screen should be scrollable to show all data
//TODO: form all the required data about the business
//TODO: form all the required data about the contest
//TODO: participate button with logic - show "register" if not registered, and "unregister" if already registered.
//TODO: if user has reached max amount of contests (should be const, currently #3) - disable the button.
//TODO: suggestion: show a dialog explaining why the button is disabled (?)

class ContestDetailsFragment : GivolFragment(), GivolToolbar.ActionListener, SupportsOnBackPressed {

    private val viewModel by viewModel<ContestDetailsViewModel>()
    private val mainViewModel by sharedViewModel<MainViewModel>()

    private lateinit var transferInfo: TransferInfo
    private lateinit var contest: FBContest
    private lateinit var picturesAdapter: PicturesAdapter

    private var countDownTimer: CountDownTimer? = null
    private var isUserRegistered = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_contest_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transferInfo = castArguments(TransferInfo::class.java)
        contest = transferInfo.contest
        configureToolbar()
        configureScreen()

    }

    private fun configureToolbar() {
        toolbar.titleTextView.text = contest.businessName
        toolbar.addActions(arrayOf(Action.BackWhite), this)
    }

    private fun configureScreen() {
        configurePictures()
        configureTexts()
        checkParticipationStatus()
        actionBtn.setOnClickListener { onActionBtnClicked() }
        businessBtn.setOnClickListener { onBusinessBtnClicked() }
    }

    private fun configurePictures() {
        picturesAdapter = PicturesAdapter()
        viewPager.adapter = picturesAdapter
        picturesAdapter.setItems(contest.details.pictures)

        GlideApp.with(this).load(contest.logo)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.background_gray)
            .into(logoIv)
    }

    private fun configureTexts() {
        participantsTv.text = resources.getString(
            R.string.participants_data, contest.participantsIdList.size, contest.minParticipants)
        amountTv.text = resources.getString(R.string.amount_data, contest.prizes.primary.value)

        with(contest.details) {
            title1.setTexts(titleOne, descriptionOne)
            title2.setTexts(titleTwo, descriptionTwo)
            title3.setTexts(titleThree, descriptionThree)
        }
    }

    override fun onResume() {
        super.onResume()
        setCountDownTimer()
    }

    private fun setCountDownTimer() {
        countDownTimer = object : CountDownTimer(contest.times.dateEnd.time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val dateWithSeconds = DateTimeHelper.getDateWithSeconds(millisUntilFinished / 1000)
                timerTv.text = dateWithSeconds
            }

            override fun onFinish() {
                timerTv.text = getString(R.string.contest_began)
            }
        }.start()
    }

    private fun cancelTimer() {
        countDownTimer?.cancel()
    }

    override fun onPause() {
        super.onPause()
        cancelTimer()
    }

    //region registration logic
    private fun checkParticipationStatus() {
        val userData = transferInfo.user
        Timber.i("mark - user = $userData")
        if (userData.contests.active.contains(transferInfo.contest.contestID)) {
            // User is already registered to this contest
            setUserIsRegistered()
        }
    }

    private fun setUserIsRegistered() {
        toggleUserRegistrationState(true)
        actionBtn.text = resources.getString(R.string.un_register_to_contest)
    }

    //endregion

    private fun onActionBtnClicked() {
        if (isUserRegistered) {
            performUnRegistrationFlow()
        } else {
            performRegistrationFlow()
        }
    }

    private fun performUnRegistrationFlow() {
        actionBtn.text = resources.getString(R.string.register_to_contest)
        viewModel.unregisterFromContest(transferInfo.uid, transferInfo.contest.contestID)
        toggleUserRegistrationState(false)

        getUserData()
    }

    private fun getUserData() {
        mainViewModel.getUserData(transferInfo.uid).observe(viewLifecycleOwner, Observer<FBUser> {
            Timber.i("mark - checkStatus = $it")
            transferInfo.user = it
        })
    }

    private fun performRegistrationFlow() {
        // Check if user is eligible to register to a contest
        if (checkRegistrationEligibility()) {
            actionBtn.text = resources.getString(R.string.un_register_to_contest)
            viewModel.registerToContest(transferInfo.uid, transferInfo.contest.contestID)
            toggleUserRegistrationState(true)
            getUserData()
        } else {
            showNonEligibilityDialog()
        }
    }

    private fun showNonEligibilityDialog() {
        val dialog = GivolDialog(
            title = resources.getString(R.string.non_eligibility_dialog_title),
            subtitle = resources.getString(R.string.non_eligibility_dialog_subtitle, MAX_CONTESTS_REGISTRATION),
            iconDrawable = R.drawable.ic_warning_black,
            positiveButtonText = R.string.dialog_positive_ok
        )

        dialog.show(parentFragmentManager, GivolDialog.TAG)
    }

    private fun onBusinessBtnClicked() {
        navigator.replace(BusinessDetailsScreen(transferInfo))
    }

    /**
     * User is eligible to register to a contest if he has no more than #3 contests running simultaneously to his name.
     */
    private fun checkRegistrationEligibility(): Boolean {
        return transferInfo.user.contests.active.size < MAX_CONTESTS_REGISTRATION
    }

    private fun toggleUserRegistrationState(isRegistered: Boolean) {
        this.isUserRegistered = isRegistered
    }
}
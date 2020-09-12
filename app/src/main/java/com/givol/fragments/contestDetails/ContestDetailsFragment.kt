package com.givol.fragments.contestDetails

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
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
import com.givol.utils.GlideApp
import com.givol.widgets.GivolToolbar
import kotlinx.android.synthetic.main.fragment_contest_details.*
import kotlinx.android.synthetic.main.givol_toolbar.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

//TODO: viewpager with pictures of the business/contest/promotion
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
        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                viewPagerIndicator.setSelected(position)
            }

        })

        picturesAdapter.setItems(contest.details.pictures)

        GlideApp.with(this).load(contest.logo)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.background_gray)
            .into(logoIv)
    }

    private fun configureTexts() {
        participantsTv.text = resources.getString(
            R.string.participants_data, contest.participantsMap.size, contest.minParticipants)
        amountTv.text = resources.getString(R.string.amount_data, contest.prizes.primary.value)

        with(contest.details) {
            title1.setTexts(titleOne, descriptionOne)
            title2.setTexts(titleTwo, descriptionTwo)
            title3.setTexts(titleThree, descriptionThree)
        }

        contestIDTv.setData(contest.contestID)
    }

    override fun onResume() {
        super.onResume()
        setCountDownTimer()
    }

    private fun setCountDownTimer() {
        countDownTimer = object : CountDownTimer(contest.times.dateEnd.time - Date().time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val dateWithSeconds = millisUntilFinished / 1000
                timerTv.text = dateWithSeconds.toString()
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
        actionBtn.text = resources.getString(R.string.already_registered_to_contest)
        actionBtn.isEnabled = false
    }

    //endregion

    private fun onActionBtnClicked() {
        if (!isUserRegistered) {
            performRegistrationFlow()
        }
    }

    private fun getUserData() {
        mainViewModel.getUserData(transferInfo.uid).observe(viewLifecycleOwner, Observer {
            it?.let {
                transferInfo.user = it
            }
        })
    }

    private fun performRegistrationFlow() {
        // Check if user is eligible to register to a contest
        if (checkRegistrationEligibility()) {
            actionBtn.text = resources.getString(R.string.already_registered_to_contest)
            viewModel.registerToContest(transferInfo.uid, transferInfo.contest)
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

    override fun showProgressView() {
        TODO("Not yet implemented")
    }

    override fun hideProgressView() {
        TODO("Not yet implemented")
    }
}
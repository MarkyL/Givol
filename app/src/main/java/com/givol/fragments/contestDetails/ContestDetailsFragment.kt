package com.givol.fragments.contestDetails

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.givol.R
import com.givol.adapters.PicturesAdapter
import com.givol.core.Action
import com.givol.core.GivolFragment
import com.givol.core.SupportsOnBackPressed
import com.givol.model.FBContest
import com.givol.navigation.arguments.TransferInfo
import com.givol.utils.DateTimeHelper
import com.givol.utils.GlideApp
import com.givol.utils.Toaster
import com.givol.widgets.GivolToolbar
import kotlinx.android.synthetic.main.contest_item.view.*
import kotlinx.android.synthetic.main.fragment_contest_details.*
import kotlinx.android.synthetic.main.givol_toolbar.view.*
import kotlinx.android.synthetic.main.picture_item.view.*
import kotlinx.android.synthetic.main.horizontal_textual_data_layout.view.*
import kotlinx.android.synthetic.main.horizontal_textual_data_layout.view.titleTv
import kotlinx.android.synthetic.main.vertical_textual_data_layout.view.*

//TODO: viewpager with pictures of the business/contest/promotion
//TODO: toolbar title with business name
//TODO: screen should be scrollable to show all data
//TODO: form all the required data about the business
//TODO: form all the required data about the contest
//TODO: participate button with logic - show "register" if not registered, and "unregister" if already registered.
//TODO: if user has reached max amount of contests (should be const, currently #3) - disable the button.
//TODO: suggestion: show a dialog explaining why the button is disabled (?)
//TODO:

class ContestDetailsFragment : GivolFragment(), GivolToolbar.ActionListener, SupportsOnBackPressed {

    private lateinit var transferInfo: TransferInfo
    private lateinit var picturesAdapter: PicturesAdapter
    private lateinit var contest: FBContest

    private var countDownTimer: CountDownTimer? = null

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
}
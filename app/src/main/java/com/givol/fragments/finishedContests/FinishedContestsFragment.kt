package com.givol.fragments.finishedContests

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.givol.R
import com.givol.adapters.BaseAdapter
import com.givol.adapters.FinishedContestsAdapter
import com.givol.core.Action
import com.givol.core.GivolFragment
import com.givol.core.SupportsOnBackPressed
import com.givol.dialogs.AbstractDialog
import com.givol.dialogs.GivolDialog
import com.givol.managers.UserFirebaseManager
import com.givol.model.FBContest
import com.givol.model.FBUser
import com.givol.model.User
import com.givol.model.UserContest
import com.givol.navigation.arguments.TransferInfo
import com.givol.utils.GridSpacingItemDecoration
import com.givol.widgets.GivolToolbar
import kotlinx.android.synthetic.main.fragment_finished_contests.*
import kotlinx.android.synthetic.main.givol_toolbar.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.timerTask

class FinishedContestsFragment : GivolFragment(), GivolToolbar.ActionListener,
    SupportsOnBackPressed, BaseAdapter.AdapterListener<FBContest> {

    private val viewModel by viewModel<FinishedContestsViewModel>()

    private lateinit var transferInfo: TransferInfo
    private val finishedContestsAdapter = FinishedContestsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_finished_contests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transferInfo = castArguments(TransferInfo::class.java)

        configureToolbar()
        configureScreen()
    }

    private fun configureToolbar() {
        toolbar.titleTextView.text = resources.getString(R.string.finished_contest)
        toolbar.addActions(arrayOf(Action.BackWhite), this)
    }

    private fun configureScreen() {
        recyclerView.apply {
            this.adapter = finishedContestsAdapter
            addItemDecoration(GridSpacingItemDecoration(1, 30, true))
        }

    }

    override fun onResume() {
        super.onResume()
        getFinishedContests()
    }


    private fun getFinishedContests() {
        showProgressView()
        viewModel.getUserFinishedContests(transferInfo.uid)
            .observe(viewLifecycleOwner, Observer<FBUser> {
                hideProgressView()
                onUserDataFetched(it)
            })
    }

    private fun onUserDataFetched(fbUser: FBUser) {
        val userFinishedContests = fbUser.contests.finished
        finishedContestsAdapter.userFinishedContests = userFinishedContests

        if (userFinishedContests.isEmpty()) {
            showNoFinishedContests()
        }

        finishedContestsAdapter.submitList(userFinishedContests.values.toList())

    }

    private fun showNoFinishedContests() {
        emptyStateTV.visibility = View.VISIBLE
    }

    private fun createUserFinishedFBContests(
        userFinishedContests: HashMap<String, UserContest>,
        fbContests: List<FBContest>
    ) {
        val list = mutableListOf<FBContest>()
        fbContests.forEach { fbContest ->
            if (userFinishedContests.containsKey(fbContest.contestID)) {
                list.add(fbContest)
            }
        }
        finishedContestsAdapter.submitList(list)
    }

    override fun showProgressView() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressView() {
        progressBar.visibility = View.GONE
    }

    override fun onItemClick(data: FBContest) {
        if (data.used) return

        val dialog = GivolDialog(
            title = resources.getString(R.string.use_contest_reward_title),
            subtitle = resources.getString(R.string.use_contest_reward_subtitle),
            positiveButtonText = R.string.use_contest_reward_positive,
            negativeButtonText = R.string.use_contest_reward_negative,
            iconDrawable = R.drawable.ic_warning_black,
            callback = object : AbstractDialog.Callback {
                override fun onDialogPositiveAction(requestCode: Int) {
                    data.used = true
                    viewModel.useContestReward(transferInfo.uid, data)
                    getFinishedContests()
                }

                override fun onDialogNegativeAction(requestCode: Int) {
                    Timber.i("onDialogNegativeAction")
                }
            })

        dialog.show(parentFragmentManager, GivolDialog.TAG)
    }
}
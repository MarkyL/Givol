package com.givol.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.givol.R
import com.givol.activities.MainActivity
import com.givol.adapters.BaseAdapter
import com.givol.adapters.ContestsAdapter
import com.givol.core.AbstractAction
import com.givol.core.Action
import com.givol.core.GivolFragment
import com.givol.core.SupportsOnBackPressed
import com.givol.model.FBContest
import com.givol.navigation.arguments.TransferInfo
import com.givol.screens.ContestDetailsScreen
import com.givol.utils.DateTimeHelper
import com.givol.utils.FirebaseUtils
import com.givol.utils.GridSpacingItemDecoration
import com.givol.widgets.GivolToolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.givol_toolbar.view.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainFragment : GivolFragment(), GivolToolbar.ActionListener, SupportsOnBackPressed,
    BaseAdapter.AdapterListener<FBContest> {

    lateinit var transferInfo: TransferInfo
    private val fbUtil: FirebaseUtils by inject()

    private val contestsAdapter = ContestsAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transferInfo = castArguments(TransferInfo::class.java)

        configureToolbar()
        recyclerView.apply {
            this.adapter = contestsAdapter
            addItemDecoration(GridSpacingItemDecoration(1, 30, true))
        }
    }

    private fun configureToolbar() {
        homeToolbar.titleTextView.text = resources.getString(R.string.app_name)
        homeToolbar.addActions(arrayOf(Action.Drawer), this)

    }

    override fun onResume() {
        super.onResume()

        val dbReference = fbUtil.getFirebaseActiveContestsNodeReference(this)
        dbReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Timber.i("getFirebaseActiveContestsNodeReference onCancelled")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Timber.i("getFirebaseActiveContestsNodeReference onDataChange - $snapshot")
                val fbContestList = mutableListOf<FBContest>()
                for(childSnapShot in snapshot.children) {
                    val element = childSnapShot.getValue(FBContest::class.java)
                    element?.let {
                        fbContestList.add(element)
                    }
                }
                Timber.i("fbContestsList = $fbContestList")

                inflateContestListWithDates(fbContestList)
                val sortedContestsByDate = fbContestList.sortedBy { it.times.dateStart }
                contestsAdapter.submitList(sortedContestsByDate)
            }

        })
    }

    private fun inflateContestListWithDates(contestList: MutableList<FBContest>) {
        for (contest in contestList) {
            val start = contest.times.dateStartStr
            DateTimeHelper.getDateFormat(start)?.let {
                contest.times.dateStart = it
            }

            val end = contest.times.dateEndStr
            DateTimeHelper.getDateFormat(end)?.let {
                contest.times.dateEnd = it
            }
        }
    }

    override fun onActionSelected(action: AbstractAction): Boolean {
        if (action == Action.Drawer) {
            Timber.i("onActionSelected - Drawer")
            (activity as MainActivity).openDrawer()
            return true
        }

        return false
    }

    private fun showProgressView() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressView() {
        progressBar.visibility = View.GONE
    }

    override fun onItemClick(data: FBContest) {
        val transferInfo = TransferInfo()
        transferInfo.contest = data
        navigator.replace(ContestDetailsScreen(transferInfo))
    }
}
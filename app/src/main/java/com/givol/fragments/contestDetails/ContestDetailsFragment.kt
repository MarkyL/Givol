package com.givol.fragments.contestDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.givol.R
import com.givol.adapters.PicturesAdapter
import com.givol.core.Action
import com.givol.core.GivolFragment
import com.givol.core.SupportsOnBackPressed
import com.givol.navigation.arguments.TransferInfo
import com.givol.utils.Toaster
import com.givol.widgets.GivolToolbar
import kotlinx.android.synthetic.main.fragment_contest_details.*
import kotlinx.android.synthetic.main.givol_toolbar.view.*

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contest_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transferInfo = castArguments(TransferInfo::class.java)
        configureToolbar()

        Toaster.show(this, transferInfo.contest.title)

        picturesAdapter = PicturesAdapter()
        viewPager.adapter = picturesAdapter
        picturesAdapter.setItems(transferInfo.contest.pictures)
    }

    private fun configureToolbar() {
        toolbar.titleTextView.text = transferInfo.contest.businessName
        toolbar.addActions(arrayOf(Action.BackWhite), this)
    }
}
package com.givol.fragments.contestDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.givol.R
import com.givol.core.Action
import com.givol.core.GivolFragment
import com.givol.core.SupportsOnBackPressed
import com.givol.navigation.arguments.TransferInfo
import com.givol.utils.Toaster
import com.givol.widgets.GivolToolbar
import kotlinx.android.synthetic.main.fragment_contest_details.*
import kotlinx.android.synthetic.main.givol_toolbar.view.*

class ContestDetailsFragment : GivolFragment(), GivolToolbar.ActionListener, SupportsOnBackPressed {

    private lateinit var transferInfo: TransferInfo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_contest_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transferInfo = castArguments(TransferInfo::class.java)
        configureToolbar()

        Toaster.show(this, transferInfo.contest.title)
    }

    private fun configureToolbar() {
        toolbar.titleTextView.text = transferInfo.contest.businessName

    }
}
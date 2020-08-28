package com.givol.fragments.businessDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.givol.R
import com.givol.core.Action
import com.givol.core.GivolFragment
import com.givol.core.SupportsOnBackPressed
import com.givol.model.FBBusiness
import com.givol.navigation.arguments.TransferInfo
import com.givol.widgets.GivolToolbar
import kotlinx.android.synthetic.main.fragment_business_details.*
import kotlinx.android.synthetic.main.givol_toolbar.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BusinessDetailsFragment : GivolFragment(), GivolToolbar.ActionListener,
    SupportsOnBackPressed {

    private val viewModel by viewModel<BusinessDetailsViewModel>()

    private lateinit var transferInfo: TransferInfo
    private lateinit var business: FBBusiness

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_business_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transferInfo = castArguments(TransferInfo::class.java)

        //showProgressView()
        viewModel.getBusinessDetails(transferInfo.contest.businessID)
            .observe(viewLifecycleOwner, Observer<FBBusiness> {
                business = it
                configureScreen()
                //hideProgressView()
            })

        configureToolbar()
    }

    private fun configureToolbar() {
        toolbar.titleTextView.text = transferInfo.contest.businessName
        toolbar.addActions(arrayOf(Action.BackWhite), this)
    }

    private fun configureScreen() {
        configurePictures()
        configureTexts()

    }

    private fun configurePictures() {

    }

    private fun configureTexts() {
        tempTxt.text = business.website
    }

    override fun showProgressView() {
        TODO("Not yet implemented")
    }

    override fun hideProgressView() {
        TODO("Not yet implemented")
    }

}
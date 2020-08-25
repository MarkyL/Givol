package com.givol.screens

import com.givol.fragments.businessDetails.BusinessDetailsFragment
import com.givol.navigation.Screen
import com.givol.navigation.arguments.TransferInfo

class BusinessDetailsScreen(transferInfo: TransferInfo) : Screen(transferInfo) {

    override fun create(): BusinessDetailsFragment {
        return BusinessDetailsFragment()
    }

}
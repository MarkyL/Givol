package com.givol.screens

import com.givol.fragments.contestDetails.ContestDetailsFragment
import com.givol.navigation.Screen
import com.givol.navigation.arguments.TransferInfo

class ContestDetailsScreen(transferInfo: TransferInfo) : Screen(transferInfo) {

    override fun create(): ContestDetailsFragment {
        return ContestDetailsFragment()
    }

}
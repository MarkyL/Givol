package com.givol.screens

import com.givol.fragments.finishedContests.FinishedContestsFragment
import com.givol.navigation.Screen
import com.givol.navigation.arguments.TransferInfo

class FinishedContestsScreen(transferInfo: TransferInfo) : Screen(transferInfo) {

    override fun create(): FinishedContestsFragment {
        return FinishedContestsFragment()
    }

}
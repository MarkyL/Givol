package com.givol.screens

import com.givol.fragments.main.MainFragment
import com.givol.navigation.Screen
import com.givol.navigation.arguments.TransferInfo

class MainScreen(transferInfo : TransferInfo) : Screen(transferInfo) {

    override fun create(): MainFragment {
        return MainFragment()
    }

}
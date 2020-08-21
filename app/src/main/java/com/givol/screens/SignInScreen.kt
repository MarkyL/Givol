package com.givol.screens

import com.givol.fragments.signin.SignInFragment
import com.givol.navigation.Screen
import com.givol.navigation.arguments.TransferInfo

class SignInScreen(transferInfo : TransferInfo) : Screen(transferInfo) {

    override fun create(): SignInFragment {
        return SignInFragment()
    }

}
package com.givol.screens

import com.givol.fragments.signin.SignInFragment
import com.givol.navigation.Screen

class SignInScreen : Screen() {

    override fun create(): SignInFragment {
        return SignInFragment()
    }

}
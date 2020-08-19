package com.givol.navigation.operations

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentTransaction
import com.givol.core.GivolFragment
import com.givol.navigation.Navigator
import com.givol.navigation.Screen

class ReplaceOperation internal constructor(navigator: Navigator, screen: Screen, addToBackStack: Boolean) :
        ForwardNavigationOperation(navigator, screen, addToBackStack) {

    override fun preformOperation(ft: FragmentTransaction, @IdRes containerId: Int, fragment: GivolFragment, tag: String) {
        ft.replace(containerId, fragment, tag)
    }
}

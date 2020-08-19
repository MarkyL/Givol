package com.givol.navigation.operations

import android.content.Intent
import com.givol.navigation.Navigator
import com.givol.navigation.Screen

internal class AddBackStackOperation(navigator: Navigator, screens: List<Screen>) :
    NewIntentOperation(navigator, screens) {
    override val intentFlags: Int
        get() = Intent.FLAG_ACTIVITY_NEW_TASK
}

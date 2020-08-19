package com.givol.navigation.operations

import android.content.Intent
import android.net.Uri
import com.givol.navigation.Navigator
import com.givol.navigation.Screen

class ReplaceBackStackWithDataOperation(navigator: Navigator, screens: List<Screen>, data: Uri?) : NewIntentOperation(navigator, screens, data) {
    override val intentFlags: Int
        get() = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
}

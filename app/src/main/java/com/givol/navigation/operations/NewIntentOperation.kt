package com.givol.navigation.operations

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.givol.core.GivolActivity
import com.givol.navigation.Navigator
import com.givol.navigation.Screen

abstract class NewIntentOperation @JvmOverloads constructor(
    navigator: Navigator,
    private val screens: List<Screen>,
    private val hostActivityClass: Class<out Activity>? = null,
    private var data: Uri? = null
) : ActivityOperation(navigator) {

    constructor(navigator: Navigator, screens: List<Screen>, data: Uri?) : this(
        navigator,
        screens,
        null,
        null
    ) {
        this.data = data
    }

    abstract val intentFlags: Int

    override fun execute(activity: GivolActivity) {
        val intent = Intent(activity, hostActivityClass ?: activity.javaClass)
        intent.data = data
        Screen.serialize(intent, screens)
        intent.addFlags(intentFlags)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activity.startActivity(intent)
        activity.overridePendingTransition(getEnterAnimation(), getExitAnimation())
        activity.finish()
    }
}

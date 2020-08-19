package com.givol.navigation.operations

import com.givol.core.GivolActivity
import com.givol.navigation.Navigator

abstract class ActivityOperation(navigator: Navigator) : Operation(navigator) {
    abstract fun execute(activity: GivolActivity)
}

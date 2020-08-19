package com.givol.navigation.operations

import androidx.fragment.app.FragmentManager
import com.givol.navigation.Navigator

abstract class FragmentOperation internal constructor(navigator: Navigator) : Operation(navigator) {
    internal abstract fun execute(fm: FragmentManager)
}

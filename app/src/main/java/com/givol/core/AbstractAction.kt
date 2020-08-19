package com.givol.core

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface AbstractAction {
    @get:DrawableRes val iconResId: Int
    @get:StringRes val titleResId: Int
    val isNavigation: Boolean
}

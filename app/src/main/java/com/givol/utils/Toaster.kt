package com.givol.utils

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.givol.BuildConfig

object Toaster {
    @JvmStatic
    fun show(context: Context, text: String, debugOnly: Boolean = true) {
        if (debugOnly && !BuildConfig.DEBUG) return

        val toast = Toast.makeText(context, text, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0, 0)
        toast.show()
    }

    @JvmStatic
    fun show(context: Context, resId: Int, debugOnly: Boolean = true) {
        show(context, context.getString(resId), debugOnly)
    }

    @JvmStatic
    fun show(fragment: Fragment?, text: String, debugOnly: Boolean = true) {
        fragment?.context?.let {
            show(it, text, debugOnly)
        }
    }

    @JvmStatic
    fun show(fragment: Fragment?, resId: Int, debugOnly: Boolean = true) {
        fragment?.context?.let {
            show(it, it.getString(resId), debugOnly)
        }
    }
}

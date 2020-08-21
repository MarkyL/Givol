package com.givol.core

import android.content.Context
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.fragment.app.Fragment
import com.givol.navigation.ActivityNavigator
import com.givol.navigation.Arguments
import com.givol.utils.ErrorHandler
import com.givol.widgets.GivolToolbar
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

import timber.log.Timber

abstract class GivolFragment : Fragment(), GivolToolbar.ActionListener, SupportsOnBackPressed {

    var givolArguments: Arguments? = null
    @AnimRes
    private var nextExitAnimation = -1
    @AnimRes
    private var nextEnterAnimation = -1

    val navigator: ActivityNavigator = get()
    val errorHandler: ErrorHandler by inject()

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {

        if (nextEnterAnimation != -1 && enter) {
            try {
                return AnimationUtils.loadAnimation(context, nextEnterAnimation)
            } finally {
                nextEnterAnimation = -1
            }
        }

        if (nextExitAnimation != -1 && !enter) {
            try {
                return AnimationUtils.loadAnimation(context, nextExitAnimation)
            } finally {
                nextExitAnimation = -1
            }
        }

        return super.onCreateAnimation(transit, enter, nextAnim)
    }

    fun overrideNextEnterAnimation(@AnimRes animation: Int) {
        this.nextEnterAnimation = animation
    }

    fun overrideNextExitAnimation(@AnimRes animation: Int) {
        this.nextExitAnimation = animation
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.let {
            if (it.containsKey(ARGUMENTS)) {
                givolArguments = it.getString(ARGUMENTS)?.let { it1 -> Arguments.deserialize(it1) }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        activity?.applicationContext?.let {
            GivolApplication.setLocale(it)
        }
    }

    fun setArguments(arguments: String) {
        val args = Bundle()
        args.putString(ARGUMENTS, arguments)
        setArguments(args)
    }

    protected fun <T : Arguments> castArguments(clazz: Class<T>): T {
        if (clazz.isInstance(givolArguments)) {
            return clazz.cast(givolArguments)!!
        }

        throw RuntimeException("This fragment does not support arguments")
    }

    open val isDrawerEnabled: Boolean
        get() = false

    override fun onActionSelected(action: AbstractAction): Boolean {
        if (action == Action.BackBlack) {
            Timber.i("onActionSelected - BackBlack")
            activity?.onBackPressed()
            return true
        }
        return false
    }

    override fun onBackPressed(): Boolean {
        return true
    }

    companion object {
        private const val ARGUMENTS = "arguments"
    }
}

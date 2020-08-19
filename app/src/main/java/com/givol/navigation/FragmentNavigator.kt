package com.givol.navigation

import android.content.Intent
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import bolts.Continuation
import bolts.Task
import com.givol.navigation.operations.*
import com.givol.utils.ObjectDelegate
import timber.log.Timber

open class FragmentNavigator : Navigator {
    private val fragmentManagerDelegate = ObjectDelegate<FragmentManager>()

    constructor(@IdRes containerId: Int, @AnimRes enterAnimation: Int, @AnimRes exitAnimation: Int,
                @AnimRes popEnterAnimation: Int, @AnimRes popExitAnimation: Int) :
            super(containerId, enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)

    constructor(@IdRes containerId: Int) : super(containerId)

    fun <T : Screen> replace(screen: T) {
        replace(screen, true)
    }

    fun <T : Screen> replace(screen: T, addToBackStack: Boolean) {
        execute(
            ReplaceOperation(
                this,
                screen,
                addToBackStack
            )
        )
    }

    fun takeFragmentManager(fragmentManager: FragmentManager) {
        fragmentManagerDelegate.takeObject(fragmentManager)
    }

    fun dropFragmentManager() {
        fragmentManagerDelegate.dropObject()
    }

    fun goBackTo(screenClass: Class<out Screen>) {
        execute(GoBackToScreenOperation(this, screenClass))
    }

    fun goBackToWithRevertedAnimations(screenClass: Class<out Screen>) {
        execute(
            GoBackToScreenOperation(
                this,
                screenClass,
                true
            )
        )
    }

    fun recreateBackStack(intent: Intent) {
        execute(RecreateBackStackOperation(this, intent))
    }

    fun <T : Screen> add(screen: T) {
        add(screen, true)
    }

    fun <T : Screen> add(screen: T, addToBackStack: Boolean) {
        execute(AddOperation(this, screen, addToBackStack))
    }

    @JvmOverloads fun remove(screenClass: Class<out Screen>, executePendingTransactions: Boolean = false) {
        execute(
            RemoveScreenOperation(
                this,
                screenClass,
                executePendingTransactions
            )
        )
    }

    override fun execute(operation: Operation) {
        if (operation is FragmentOperation) {

            fragmentManagerDelegate.waitForObject().onSuccess(Continuation<FragmentManager, Void> { task ->
                operation.execute(task.result)
                null
            }, Task.UI_THREAD_EXECUTOR).continueWith { task ->
                if (task.isFaulted) {
                    Timber.e(task.error, "navigation failed")
                }
                null
            }
        }
    }

    override val fragmentManager: Task<FragmentManager>
        get() = fragmentManagerDelegate.waitForObject()
}

package com.givol.navigation.operations

import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import com.givol.core.GivolFragment
import com.givol.navigation.Navigator

import rx.subjects.PublishSubject

abstract class Operation internal constructor(private val navigator: Navigator) {
    private val subject: PublishSubject<GivolFragment> = navigator.subject
    @IdRes internal val containerId: Int = navigator.containerId
    @AnimRes private var enterAnimation: Int = 0
    @AnimRes private var exitAnimation: Int = 0
    @AnimRes private var popEnterAnimation: Int = 0
    @AnimRes private var popExitAnimation: Int = 0

    init {
        enterAnimation = navigator.enterAnimation
        exitAnimation = navigator.exitAnimation
        popEnterAnimation = navigator.popEnterAnimation
        popExitAnimation = navigator.popExitAnimation
    }

    protected fun onNext(fragment: GivolFragment) {
        subject.onNext(fragment)
    }

    fun withAnimations(
        @AnimRes enterAnimation: Int, @AnimRes exitAnimation: Int,
        @AnimRes popEnterAnimation: Int, @AnimRes popExitAnimation: Int
    ): Operation {
        return setEnterAnimation(enterAnimation)
            .setExitAnimation(exitAnimation)
            .setPopEnterAnimation(popEnterAnimation)
            .setPopExitAnimation(popExitAnimation)
    }

    fun execute() {
        navigator.execute(this)
    }

    fun getEnterAnimation(): Int = enterAnimation

    fun getExitAnimation(): Int = exitAnimation

    fun getPopEnterAnimation(): Int = popEnterAnimation

    fun getPopExitAnimation(): Int = popExitAnimation

    fun setEnterAnimation(enterAnimation: Int): Operation {
        this.enterAnimation = enterAnimation
        return this
    }

    fun setExitAnimation(exitAnimation: Int): Operation {
        this.exitAnimation = exitAnimation
        return this
    }

    fun setPopEnterAnimation(popEnterAnimation: Int): Operation {
        this.popEnterAnimation = popEnterAnimation
        return this
    }

    fun setPopExitAnimation(popExitAnimation: Int): Operation {
        this.popExitAnimation = popExitAnimation
        return this
    }
}

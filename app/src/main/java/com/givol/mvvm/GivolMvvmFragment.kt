package com.givol.mvvm

import android.os.Bundle
import androidx.lifecycle.Observer
import com.givol.core.GivolFragment
import timber.log.Timber

abstract class GivolMvvmFragment<T, R> : GivolFragment() {
    lateinit var viewModel: BaseViewModel<T, R>
//    @Inject lateinit var baseViewModelFactory: BaseViewModelFactory
//    @Inject lateinit var softKeyboard: SoftKeyboard
//    @Inject lateinit var errorHandler: ErrorHandler
//    @Inject lateinit var appsFlyerHelper: AppsFlyerHelper
//    @Inject lateinit var analytics: Analytics

    abstract fun setViewModel()
    abstract fun onNext(result: T?)
    abstract fun init()
    abstract fun onLoading()
    abstract fun onComplete()
    abstract fun onError(result: T?, throwable: Throwable?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewModel()
        observerLiveData()
    }

    private fun observerLiveData() {
        viewModel.stateStream.observe(this, Observer<LiveState<T>> { t ->
            t?.let {
                when (it) {
                    is Init -> {
                        Timber.i("Init: ")
                        init()
                    }
                    is Loading -> {
                        Timber.i("Loading: ")
                        onLoading()
                    }
                    is Next -> {
                        Timber.i("Next: ${(it.result)}")
                        onNext(it.result)
                    }
                    is Error -> {
                        Timber.i("Error:")
                        onError(it.result, it.throwable)
                    }
                    is Completed -> {
                        Timber.i("Completed:")
                        onComplete()
                    }
                }
            }
        })
    }

}
package com.givol.fragments.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.givol.model.FBContest
import com.givol.mvvm.BaseViewModel
import com.givol.utils.Event
import com.givol.managers.ContestsFirebaseManager
import timber.log.Timber

class MainViewModel constructor(
    application: Application,
    private val firebaseManager: ContestsFirebaseManager
)
    : BaseViewModel<Event<MainDataState>, MainDataEvent>(application = application) {

    override fun handleScreenEvents(event: MainDataEvent) {
        Timber.i("dispatchScreenEvent: ${event.javaClass.simpleName}")
        when (event) {
            is GetContests -> getContests()
        }
    }

    fun getContests(): MutableLiveData<List<FBContest>> {
        firebaseManager.addSingleContestListener()
        return firebaseManager.contestsLiveData
    }

}

// Events = actions coming from UI
sealed class MainDataEvent
object GetContests : MainDataEvent()

sealed class MainDataState
//data class GetContestsSuccess(val contestsList: List<FBContest>) : MainDataState()
//object GetContestsFailure : MainDataState()




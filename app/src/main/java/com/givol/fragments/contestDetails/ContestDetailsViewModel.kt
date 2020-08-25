package com.givol.fragments.contestDetails

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.givol.managers.UserFirebaseManager
import com.givol.model.FBUser
import com.givol.mvvm.BaseViewModel
import com.givol.utils.Event
import timber.log.Timber

class ContestDetailsViewModel constructor(
    application: Application,
    private val userFirebaseManager: UserFirebaseManager) : BaseViewModel<Event<ContestDetailsState>, ContestDetailsEvent>(application = application) {

    override fun handleScreenEvents(event: ContestDetailsEvent) {
        Timber.i("dispatchScreenEvent: ${event.javaClass.simpleName}")
    }

    fun registerToContest(uid: String, contestID: String) {
        userFirebaseManager.registerFromContest(uid, contestID)
    }

    fun unregisterFromContest(uid: String, contestID: String) {
        userFirebaseManager.unregisterFromContest(uid, contestID)
    }

}

// Events = actions coming from UI
sealed class ContestDetailsEvent

sealed class ContestDetailsState




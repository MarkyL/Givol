package com.givol.fragments.finishedContests

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.givol.managers.ContestsFirebaseManager
import com.givol.managers.UserFirebaseManager
import com.givol.model.FBContest
import com.givol.model.FBUser
import com.givol.mvvm.BaseViewModel
import com.givol.utils.Event
import timber.log.Timber

class FinishedContestsViewModel constructor(
    application: Application,
    private val userManager: UserFirebaseManager,
    private val contestManager: ContestsFirebaseManager
) : BaseViewModel<Event<ContestDetailsState>, ContestDetailsEvent>(application = application) {

    override fun handleScreenEvents(event: ContestDetailsEvent) {
        Timber.i("dispatchScreenEvent: ${event.javaClass.simpleName}")
    }

    fun getUserFinishedContests(uid: String): MutableLiveData<FBUser> {
        userManager.addSingleContestListener(uid)
        return userManager.userLiveData
    }

    fun getFinishedContests(): MutableLiveData<List<FBContest>> {
        contestManager.addSingleFinishedContestListener()
        return contestManager.finishedContestLiveData
    }

//    fun getContestDataByID(contestId: String): MutableLiveData<FBContest> {
//        Timber.i("mark getContestDataByID - $contestId")
//        contestManager.addSingleFinishedContestListenerByID(contestId)
//        return contestManager.finishedContestLiveData
//    }


}

// Events = actions coming from UI
sealed class ContestDetailsEvent

sealed class ContestDetailsState




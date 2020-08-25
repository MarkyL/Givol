package com.givol.fragments.businessDetails

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.givol.managers.BusinessFirebaseManager
import com.givol.model.FBContest
import com.givol.mvvm.BaseViewModel
import com.givol.utils.Event
import com.givol.managers.ContestsFirebaseManager
import com.givol.managers.UserFirebaseManager
import com.givol.model.FBBusiness
import com.givol.model.FBUser
import timber.log.Timber

class BusinessDetailsViewModel constructor(
    application: Application,
    private val businessManager: BusinessFirebaseManager)
    : BaseViewModel<Event<MainDataState>, MainDataEvent>(application = application) {

    override fun handleScreenEvents(event: MainDataEvent) {
        Timber.i("dispatchScreenEvent: ${event.javaClass.simpleName}")
    }

    fun getBusinessDetails(businessId: String): MutableLiveData<FBBusiness> {
        businessManager.addSingleBusinessListener(businessId)
        return businessManager.businessLiveData
    }

}

// Events = actions coming from UI
sealed class MainDataEvent
object GetContests : MainDataEvent()

sealed class MainDataState
//data class GetContestsSuccess(val contestsList: List<FBContest>) : MainDataState()
//object GetContestsFailure : MainDataState()




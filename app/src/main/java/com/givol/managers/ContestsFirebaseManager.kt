package com.givol.managers

import androidx.lifecycle.MutableLiveData
import com.givol.model.FBContest
import com.givol.utils.DateTimeHelper
import com.givol.utils.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

object ContestsFirebaseManager : KoinComponent {

    private val fbUtil: FirebaseUtils by inject()

    // Contests
    val contestsLiveData = MutableLiveData<List<FBContest>>()
    private val contestsListener: ValueEventListener
    private val contestsReference: DatabaseReference

    init {
        contestsListener =
            createContestsListener()
        contestsReference =
            FirebaseUtils.getFirebaseActiveContestsNodeReference()
    }

    //region Contests
    private fun createContestsListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Timber.i("getFirebaseActiveContestsNodeReference onDataChange - $snapshot")
                val fbContestList = mutableListOf<FBContest>()
                for (childSnapShot in snapshot.children) {
                    val element = childSnapShot.getValue(FBContest::class.java)
                    element?.let {
                        fbContestList.add(element)
                    }
                }
                Timber.i("fbContestsList = $fbContestList")

                inflateContestListWithDates(
                    fbContestList
                )
                val sortedContestsByDate = fbContestList.sortedBy { it.times.dateStart }

                contestsLiveData.value = sortedContestsByDate
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e(error.toException(), "Failed to getData from documents")
            }
        }
    }

    private fun inflateContestListWithDates(contestList: MutableList<FBContest>) {
        for (contest in contestList) {
            val start = contest.times.dateStartStr
            DateTimeHelper.getDateFormat(start)?.let {
                contest.times.dateStart = it
            }

            val end = contest.times.dateEndStr
            DateTimeHelper.getDateFormat(end)?.let {
                contest.times.dateEnd = it
            }
        }
    }

    fun addSingleContestListener() {
        contestsReference.addListenerForSingleValueEvent(contestsListener)
    }

    //endregion
}
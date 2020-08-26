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
    val contestsLiveData = MutableLiveData<HashMap<String, FBContest>>()
    private val activeContestsListener: ValueEventListener
    private val contestsReference: DatabaseReference

    init {
        activeContestsListener =
            createActiveContestsListener()
        contestsReference =
            FirebaseUtils.getFirebaseActiveContestsNodeReference()
    }

    //region Contests
    private fun createActiveContestsListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Timber.i("getFirebaseActiveContestsNodeReference onDataChange - $snapshot")
                val fbContestMap = snapshot.value as HashMap<String, FBContest>//hashMapOf<String, FBContest>()
//                for (childSnapShot in snapshot.children) {
//                    val element = childSnapShot.getValue(FBContest::class.java)
//                    element?.let {
//                        fbContestMap.add(element)
//                    }
//                }
                Timber.i("fbContestsList = $fbContestMap")

                inflateContestListWithDates(fbContestMap)
                //val sortedContestsByDate = fbContestMap.sortedBy { it.times.dateStart }
//
                contestsLiveData.value = fbContestMap
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e(error.toException(), "Failed to getData from documents")
            }
        }
    }

    private fun inflateContestListWithDates(contestsMap: HashMap<String, FBContest>) {
        for (contest in contestsMap) {
            val start = contest.value.times.dateStartStr
            DateTimeHelper.getDateFormat(start)?.let {
                contest.value.times.dateStart = it
            }

            val end = contest.value.times.dateEndStr
            DateTimeHelper.getDateFormat(end)?.let {
                contest.value.times.dateEnd = it
            }
        }
    }

    fun addSingleContestListener() {
        contestsReference.addListenerForSingleValueEvent(activeContestsListener)
    }

    //endregion
}
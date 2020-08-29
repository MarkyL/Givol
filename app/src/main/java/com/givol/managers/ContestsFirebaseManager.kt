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
    val finishedContestLiveData = MutableLiveData<List<FBContest>>()
    private val contestsListener: ValueEventListener
    private val finishedContestListener: ValueEventListener
    private val activeContestsReference: DatabaseReference
    private val finishedContestsReference: DatabaseReference

    init {
        contestsListener =
            createContestsListener()
        activeContestsReference =
            FirebaseUtils.getFirebaseActiveContestsNodeReference()

        finishedContestListener =
            createFinishedContestListener()
        finishedContestsReference =
            FirebaseUtils.getFirebaseFinishedContestsNodeReference()
    }

    //region Contests
    private fun createContestsListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Timber.i("getFirebaseActiveContestsNodeReference onDataChange - $snapshot")
                //val sortedContestsByDate = getContestListSortedFromSnapshot(snapshot)
                contestsLiveData.value = getContestListSortedFromSnapshot(snapshot)//sortedContestsByDate
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e(error.toException(), "Failed to getData from contests")
            }
        }
    }

    private fun getContestListSortedFromSnapshot(snapshot: DataSnapshot): List<FBContest> {
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
        return fbContestList.sortedBy { it.times.dateStart }
    }

    private fun createFinishedContestListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                finishedContestLiveData.value = getContestListSortedFromSnapshot(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e(error.toException(), "Failed to getData from finished contest")
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
        activeContestsReference.addListenerForSingleValueEvent(contestsListener)
    }

    fun addSingleFinishedContestListener() {
        finishedContestsReference.addListenerForSingleValueEvent(finishedContestListener)
    }

    fun addSingleFinishedContestListenerByID(contestId: String) {
        finishedContestsReference.child(contestId).addListenerForSingleValueEvent(finishedContestListener)
    }

    fun useContestReward(uid: String, fbContest: FBContest) {
        // Update user's contest to value - used = true.
        fbUtil.getUserFinishedContestNodeReference(uid).child(fbContest.contestID).setValue(fbContest)

        finishedContestsReference.child(fbContest.contestID).child("participants")
            .child(uid).child("used").setValue(true)
    }

    //endregion
}
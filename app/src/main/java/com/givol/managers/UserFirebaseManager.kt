package com.givol.managers

import androidx.lifecycle.MutableLiveData
import com.givol.model.FBUser
import com.givol.utils.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

object UserFirebaseManager : KoinComponent {

    private val fbUtil: FirebaseUtils by inject()

    // Users
    val userLiveData = MutableLiveData<FBUser>()
    private val userListener: ValueEventListener
    private val usersReference: DatabaseReference

    init {
        userListener = createUserListener()
        usersReference = FirebaseUtils.getFirebaseUsersNodeReference()
    }

    //region Contests
    private fun createUserListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Timber.i("getFirebaseUsersNodeReference onDataChange - $snapshot")
                val userData = snapshot.getValue(FBUser::class.java)
                Timber.i("received data of user - $userData")
//                val fbContestList = mutableListOf<FBContest>()
//                for (childSnapShot in snapshot.children) {
//                    val element = childSnapShot.getValue(FBContest::class.java)
//                    element?.let {
//                        fbContestList.add(element)
//                    }
//                }
//                Timber.i("fbContestsList = $fbContestList")
//
//                inflateContestListWithDates(fbContestList)
//                val sortedContestsByDate = fbContestList.sortedBy { it.times.dateStart }
//
                userLiveData.value = userData
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e(error.toException(), "Failed to getData from documents")
            }
        }
    }

    fun addSingleContestListener(uid: String) {
        usersReference.child(uid).addListenerForSingleValueEvent(userListener)
    }

    //endregion
}
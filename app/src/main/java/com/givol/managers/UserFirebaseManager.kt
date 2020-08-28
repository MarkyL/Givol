package com.givol.managers

import androidx.lifecycle.MutableLiveData
import com.givol.model.CONTEST_WIN_STATE
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

    lateinit var uid: String

    init {
        userListener = createUserListener()
        usersReference = FirebaseUtils.getFirebaseUsersNodeReference()
    }

    //region Contests
    private fun createUserListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Timber.i("getFirebaseUsersNodeReference onDataChange - $snapshot")
//
//                val userData = FBUser()
//                with(userData.contests) {
//                    snapshot.child(fbUtil.PARAM_CONTEST).child(fbUtil.PARAM_ACTIVE_CONTEST).value?.let { active = it as HashMap<String, UserContest> }
//                    snapshot.child(fbUtil.PARAM_CONTEST).child(fbUtil.PARAM_FINISHED_CONTEST).value?.let { finished = it as HashMap<String, UserContest> }
//                }
                userLiveData.value = snapshot.child(uid).getValue(FBUser::class.java)//userData
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e(error.toException(), "Failed to getData from documents")
            }
        }
    }

    fun addSingleContestListener(uid: String) {
//        usersReference.child(uid).addListenerForSingleValueEvent(userListener)
        this.uid = uid
        usersReference.addListenerForSingleValueEvent(userListener)
    }

    fun registerFromContest(uid: String, contestID: String) {
        // user tree
        val userContest = fbUtil.getUserActiveContestNodeReference(uid).child(contestID)
        userContest.child("contest_state").setValue(CONTEST_WIN_STATE.NONE)
        userContest.child("used").setValue(false)

        // contest tree
        val currentContestForUser =
            fbUtil.getFirebaseActiveContestsNodeReference().child(contestID).child("participants")
                .child(uid)
        currentContestForUser.child("contest_state").setValue(CONTEST_WIN_STATE.NONE)
        currentContestForUser.child("used").setValue(false)
    }

    fun unregisterFromContest(uid: String, contestID: String) {
        fbUtil.getUserActiveContestNodeReference(uid).child(contestID).removeValue()
        fbUtil.getFirebaseActiveContestsNodeReference().child(contestID).child("participants")
            .child(uid).removeValue()
    }

    //endregion
}
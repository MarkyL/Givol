package com.givol.utils

import androidx.fragment.app.Fragment
import com.givol.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseUtils {

    private fun getFirebaseDatabase(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference
    }

    //region contests node related
    private fun getFirebaseContestsNodeReference(): DatabaseReference {
        return getFirebaseDatabase().child(PARAM_CONTEST)
    }

    fun getFirebaseActiveContestsNodeReference(): DatabaseReference {
        return getFirebaseContestsNodeReference().child(PARAM_ACTIVE_CONTEST)
    }

    fun getFirebaseFinishedContestsNodeReference(): DatabaseReference {
        return getFirebaseContestsNodeReference().child(PARAM_FINISHED_CONTEST)
    }

    //endregion

    //region user node related
    fun getFirebaseUsersNodeReference(): DatabaseReference {
        return getFirebaseDatabase().child(PARAM_USERS)
    }

    fun getFirebaseUserNodeReference(uid: String): DatabaseReference {
        return getFirebaseUsersNodeReference().child(uid)
    }

    fun getUserActiveContestNodeReference(uid: String): DatabaseReference {
        return getFirebaseUserNodeReference(uid).child(PARAM_CONTEST).child(PARAM_ACTIVE_CONTEST)
    }

    fun getUserFinishedContestNodeReference(uid: String): DatabaseReference {
        return getFirebaseUserNodeReference(uid).child(PARAM_CONTEST).child(PARAM_FINISHED_CONTEST)
    }
    //endregion

    //region business node related
    fun getBusinessesNodeReference(): DatabaseReference {
        return getFirebaseDatabase().child(PARAM_BUSINESS)
    }

    fun getBusinessNodeReference(bid: String): DatabaseReference {
        return getBusinessesNodeReference().child(bid)
    }
    //endregion

    // params for db data fetching.
    const val PARAM_EMAIL = "email"
    const val PARAM_CONTEST = "contests"
    const val PARAM_ACTIVE_CONTEST = "active"
    const val PARAM_FINISHED_CONTEST = "finished"
    const val PARAM_WON_CONTEST = "won"
    const val PARAM_USERS = "users"
    const val PARAM_BUSINESS = "businesses"

}
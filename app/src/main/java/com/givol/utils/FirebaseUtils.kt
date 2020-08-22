package com.givol.utils

import androidx.fragment.app.Fragment
import com.givol.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseUtils {

    fun getFirebaseDatabase(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference
    }

    fun getFirebaseUsersNodeReference(fragment: Fragment): DatabaseReference {
        return getFirebaseDatabase().child(fragment.resources.getString(R.string.users_node))
    }

    fun getFirebaseUserNodeReference(fragment: Fragment, uid: String): DatabaseReference {
        return getFirebaseUsersNodeReference(fragment).child(uid)
    }

    fun getFirebaseContestsNodeReference(fragment: Fragment): DatabaseReference {
        return getFirebaseDatabase().child(PARAM_CONTEST)
    }

    fun getFirebaseActiveContestsNodeReference(fragment: Fragment): DatabaseReference {
        return getFirebaseContestsNodeReference(fragment).child(PARAM_ACTIVE_CONTEST)
    }

    fun getFirebaseFinishedContestsNodeReference(fragment: Fragment): DatabaseReference {
        return getFirebaseContestsNodeReference(fragment).child(PARAM_FINISHED_CONTEST)
    }

    const val PARAM_EMAIL = "email"
    const val PARAM_CONTEST = "contests"
    const val PARAM_ACTIVE_CONTEST = "active"
    const val PARAM_FINISHED_CONTEST = "finished"
    const val PARAM_WON_CONTEST = "won"
}
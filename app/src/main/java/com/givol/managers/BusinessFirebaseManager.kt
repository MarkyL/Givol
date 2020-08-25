package com.givol.managers

import androidx.lifecycle.MutableLiveData
import com.givol.model.FBBusiness
import com.givol.utils.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

object BusinessFirebaseManager : KoinComponent {

    private val fbUtil: FirebaseUtils by inject()

    val businessLiveData = MutableLiveData<FBBusiness>()
    private val businessListener: ValueEventListener
    private val businessReference: DatabaseReference

    init {
        businessListener = createBusinessListener()
        businessReference = FirebaseUtils.getBusinessesNodeReference()
    }

    private fun createBusinessListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Timber.i("getFirebaseUsersNodeReference onDataChange - $snapshot")
                val businessData = snapshot.getValue(FBBusiness::class.java)
                businessLiveData.value = businessData
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e(error.toException(), "Failed to getData from documents")
            }
        }
    }

    fun addSingleBusinessListener(businessId: String) {
        businessReference.child(businessId).addListenerForSingleValueEvent(businessListener)
    }

}
package com.givol.managers

import com.google.firebase.database.FirebaseDatabase
import rx.subjects.BehaviorSubject

object FBUsersManager {

    private val database = FirebaseDatabase.getInstance()

    private val subject: BehaviorSubject<FBUsersManager> = BehaviorSubject.create()

    init {

    }
}
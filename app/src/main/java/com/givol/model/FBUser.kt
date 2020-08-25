package com.givol.model

import com.google.firebase.database.PropertyName

/**
 * A class which represents the data that Firebase holds regarding of the logged in user.
 */
data class FBUser(
    @PropertyName("contests")
    var contests: UserContests = UserContests())

class UserContests(
    @PropertyName("active") var active: HashMap<String, Boolean> = hashMapOf(),
    @PropertyName("finished") var finished: HashMap<String, Boolean> = hashMapOf(),
    @PropertyName("won") var won: HashMap<String, Boolean> = hashMapOf())

package com.givol.model

import com.google.firebase.database.PropertyName

/**
 * A class which represents the data that Firebase holds regarding of the logged in user.
 */
data class FBUser(
    @PropertyName("contests")
    var contests: UserContests = UserContests())

class UserContests(
    @PropertyName("active") var active: List<String> = listOf(),
    @PropertyName("finished") var finished: List<String> = listOf(),
    @PropertyName("won") var won: List<String> = listOf())

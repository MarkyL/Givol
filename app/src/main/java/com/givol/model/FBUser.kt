package com.givol.model

import com.google.firebase.database.PropertyName

/**
 * A class which represents the data that Firebase holds regarding of the logged in user.
 */
data class FBUser(
    @PropertyName("contests")
    var contests: UserContests = UserContests(),
    @PropertyName("email")
    var email: String = "")

class UserContests(
    @PropertyName("active") var active: HashMap<String, UserContest> = hashMapOf(),
    @PropertyName("finished") var finished: HashMap<String, UserContest> = hashMapOf())

class UserContest(
    @PropertyName("contest_state") var contestState: String = CONTEST_WIN_STATE.NONE.name,
    @PropertyName("used") var used: Boolean = false,
    @get:PropertyName("date_end") @set:PropertyName("date_end")
    var dateEndStr: String = "") {

    var contestStateEnum: CONTEST_WIN_STATE = CONTEST_WIN_STATE.NONE
        get(): CONTEST_WIN_STATE {
            return when (contestState) {
                CONTEST_WIN_STATE.NONE.name -> CONTEST_WIN_STATE.NONE
                CONTEST_WIN_STATE.WIN.name -> CONTEST_WIN_STATE.WIN
                CONTEST_WIN_STATE.COLDONSENSE.name -> CONTEST_WIN_STATE.COLDONSENSE
                else -> CONTEST_WIN_STATE.NONE
            }
        }
}
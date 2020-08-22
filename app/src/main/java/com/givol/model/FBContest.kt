package com.givol.model

import com.google.firebase.database.PropertyName

data class FBContest(
    @get:PropertyName("contest_id") @set:PropertyName("contest_id")
    var contestID: String = "",

    @get:PropertyName("business_id") @set:PropertyName("business_id")
    var businessID: String = "",

    @get:PropertyName("business_name") @set:PropertyName("business_name")
    var businessName: String = "",

    @get:PropertyName("title") @set:PropertyName("title")
    var title: String = "",

    @get:PropertyName("start_date") @set:PropertyName("start_date")
    var startDate: String = "",

    @get:PropertyName("end_date") @set:PropertyName("end_date")
    var endDate: String = "",

    @get:PropertyName("min_participants") @set:PropertyName("min_participants")
    var minParticipants: Long = 0,

    @get:PropertyName("participants") @set:PropertyName("participants")
    var participantsIdList: List<String> = listOf(),

    @PropertyName("pictures") var pictures: List<String> = listOf(),
    @PropertyName("logo") var logo: String = "",
    @PropertyName("prizes") var prizes: Prizes = Prizes()) {


}

class Prizes(
    @PropertyName("primary") var primary: Prize = Prize(),
    @PropertyName("secondary") var secondary: Prize = Prize()) {

}

class Prize(
    @PropertyName("description") var description: String = "",
    @PropertyName("value") var value: Int = 0) {

}
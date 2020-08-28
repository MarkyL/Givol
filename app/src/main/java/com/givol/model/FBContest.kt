package com.givol.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName
import java.util.*
import kotlin.collections.HashMap

data class FBContest(
    @get:PropertyName("contest_id") @set:PropertyName("contest_id")
    var contestID: String = "",

    @get:PropertyName("business_id") @set:PropertyName("business_id")
    var businessID: String = "",

    @get:PropertyName("business_name") @set:PropertyName("business_name")
    var businessName: String = "",

    @get:PropertyName("title") @set:PropertyName("title")
    var title: String = "",

    @get:PropertyName("min_participants") @set:PropertyName("min_participants")
    var minParticipants: Long = 0,

    @get:PropertyName("participants") @set:PropertyName("participants")
    var participantsMap: HashMap<String, UserContest> = hashMapOf(),

    @PropertyName("details") var details: ContestDetails = ContestDetails(),
    @PropertyName("logo") var logo: String = "",
    @PropertyName("times") var times: Times = Times(),
    @PropertyName("prizes") var prizes: Prizes = Prizes()) {}

class Prizes(
    @PropertyName("primary") var primary: Prize = Prize(),
    @PropertyName("secondary") var secondary: Prize = Prize()) {}

class Prize(
    @PropertyName("description") var description: String = "",
    @PropertyName("value") var value: Int = 0) {}

class Times(
    @get:PropertyName("date_start") @set:PropertyName("date_start")
    var dateStartStr: String = "",

    @get:PropertyName("date_end") @set:PropertyName("date_end")
    var dateEndStr: String = "",

    @get:PropertyName("time_start") @set:PropertyName("time_start")
    var timeStart: String = "",

    @get:PropertyName("time_end") @set:PropertyName("time_end")
    var timeEnd: String = "",

    @Exclude var dateStart: Date = Date(),
    @Exclude var dateEnd: Date = Date())

class ContestDetails(
    @get:PropertyName("description_one") @set:PropertyName("description_one")
    var descriptionOne: String = "",

    @get:PropertyName("description_two") @set:PropertyName("description_two")
    var descriptionTwo: String = "",

    @get:PropertyName("description_three") @set:PropertyName("description_three")
    var descriptionThree: String = "",

    @get:PropertyName("title_one") @set:PropertyName("title_one")
    var titleOne: String = "",

    @get:PropertyName("title_two") @set:PropertyName("title_two")
    var titleTwo: String = "",

    @get:PropertyName("title_three") @set:PropertyName("title_three")
    var titleThree: String = "",

    @PropertyName("pictures") var pictures: List<String> = listOf())

enum class CONTEST_WIN_STATE {
    @PropertyName("NONE") NONE,
    @PropertyName("WIN") WIN,
    @PropertyName("COLDONSENSE") COLDONSENSE
}
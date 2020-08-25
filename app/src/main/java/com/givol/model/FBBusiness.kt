package com.givol.model

import com.google.firebase.database.PropertyName

/**
 * A class which represents the data that Firebase holds regarding of the logged in user.
 */
data class FBBusiness(
    @PropertyName("address")
    var address: String = "",
    @PropertyName("city")
    var city: String = "",
    @PropertyName("description")
    var description: String = "",
    @PropertyName("email")
    var email: String = "",
    @PropertyName("name")
    var name: String = "",
    @PropertyName("phone")
    var phone: String = "",
    @PropertyName("website")
    var website: String = "")


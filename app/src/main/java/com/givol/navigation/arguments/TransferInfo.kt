package com.givol.navigation.arguments

import com.givol.navigation.Arguments

class TransferInfo(var flow: Flow = Flow.Default): Arguments() {

    var phoneNumber: String = ""

    enum class Flow {
        Default
    }
}
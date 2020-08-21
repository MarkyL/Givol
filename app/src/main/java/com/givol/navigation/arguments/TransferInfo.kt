package com.givol.navigation.arguments

import com.givol.navigation.Arguments

class TransferInfo(var flow: Flow = Flow.Default): Arguments() {
    var uid: String = ""

    enum class Flow {
        Default,
        SignUP,
        SignIn

    }
}
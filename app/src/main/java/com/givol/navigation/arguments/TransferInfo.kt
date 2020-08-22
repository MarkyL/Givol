package com.givol.navigation.arguments

import com.givol.model.FBContest
import com.givol.navigation.Arguments

class TransferInfo(var flow: Flow = Flow.Default): Arguments() {
    var uid: String = ""
    var contest: FBContest = FBContest()

    enum class Flow {
        Default,
        SignUP,
        SignIn

    }
}
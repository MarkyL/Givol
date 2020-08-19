package com.givol.fragments.main

import com.givol.core.GivolFragment
import com.givol.core.SupportsOnBackPressed
import com.givol.navigation.arguments.TransferInfo
import com.givol.widgets.GivolToolbar

class MainFragment : GivolFragment(), GivolToolbar.ActionListener, SupportsOnBackPressed {

    lateinit var transferInfo: TransferInfo
}
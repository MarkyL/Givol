package com.givol.activities

import android.os.Bundle
import com.givol.R
import com.givol.core.GivolActivity
import timber.log.Timber

class MainActivity : GivolActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.i("Test")
    }
}
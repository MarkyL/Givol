package com.givol.core

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.givol.navigation.ActivityNavigator
import org.koin.android.ext.android.get

abstract class GivolActivity : AppCompatActivity() {

    val navigator: ActivityNavigator = get()
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        // Set locale has to be before super.onCreate
        GivolApplication.setLocale(this)
        //theme.applyStyle(Preferences(this).fontStyle.resId, true)
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        GivolApplication.setLocale(this)
        super.onStart()

    }
}

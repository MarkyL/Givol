package com.givol.mvvm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.prefs.Preferences

//@Singleton
class BaseViewModelFactory constructor(
    val application: Application,
    var preferences: Preferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        throw  IllegalArgumentException("Unknown ViewModel class")
    }
}
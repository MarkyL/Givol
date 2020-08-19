package com.givol.di

import android.content.Context
import android.content.SharedPreferences
import com.givol.R
import com.givol.model.User
import com.givol.navigation.ActivityNavigator
import com.givol.navigation.FragmentNavigator
import com.givol.utils.ErrorHandler
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module(override = true) {
    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            User.CURRENT_USER_FILE_NAME,
            Context.MODE_PRIVATE
        )
    }
}

val navigatorModule = module {
    single { ActivityNavigator(R.id.fragmentContainer) }
    single { FragmentNavigator(R.id.fragmentContainer) }
}

val viewModelsModule = module {
    // viewModel { MainViewModel(get(), get()) }
}

val errorModule = module {
    single { ErrorHandler(get()) }
}

// Gather all app modules
val givolApp = listOf(
    dataModule, navigatorModule, viewModelsModule, errorModule
)
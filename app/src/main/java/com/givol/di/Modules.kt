package com.givol.di

import android.content.Context
import android.content.SharedPreferences
import com.givol.R
import com.givol.fragments.main.MainViewModel
import com.givol.managers.FBUsersManager
import com.givol.model.User
import com.givol.navigation.ActivityNavigator
import com.givol.navigation.FragmentNavigator
import com.givol.utils.ErrorHandler
import com.givol.utils.FirebaseManager
import com.givol.utils.FirebaseUtils
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
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
     viewModel { MainViewModel(get(), get()) }
}

val errorModule = module {
    single { ErrorHandler(get()) }
}

val managers = module {
    single { FBUsersManager }
}

val utils = module {
    single { FirebaseUtils }
    single { FirebaseManager }
}

// Gather all app modules
val givolApp = listOf(
    dataModule, navigatorModule, viewModelsModule, errorModule, managers, utils
)
package com.givol.di

import android.content.Context
import android.content.SharedPreferences
import com.givol.R
import com.givol.fragments.businessDetails.BusinessDetailsViewModel
import com.givol.fragments.contestDetails.ContestDetailsViewModel
import com.givol.fragments.main.MainViewModel
import com.givol.managers.BusinessFirebaseManager
import com.givol.model.User
import com.givol.navigation.ActivityNavigator
import com.givol.navigation.FragmentNavigator
import com.givol.utils.ErrorHandler
import com.givol.managers.ContestsFirebaseManager
import com.givol.utils.FirebaseUtils
import com.givol.managers.UserFirebaseManager
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
     viewModel { MainViewModel(get(), get(), get()) }
     viewModel { ContestDetailsViewModel(get(), get()) }
     viewModel { BusinessDetailsViewModel(get(), get()) }
}

val errorModule = module {
    single { ErrorHandler(get()) }
}

val managers = module {
    single { ContestsFirebaseManager }
    single { UserFirebaseManager }
    single { BusinessFirebaseManager }
}

val utils = module {
    single { FirebaseUtils }
}

// Gather all app modules
val givolApp = listOf(
    dataModule, navigatorModule, viewModelsModule, errorModule, managers, utils
)
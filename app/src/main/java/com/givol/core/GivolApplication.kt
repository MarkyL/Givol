package com.givol.core

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.SparseArray
import com.givol.di.givolApp
import com.givol.navigation.Arguments
import com.givol.navigation.Screen
import com.givol.navigation.arguments.TransferInfo
import com.givol.screens.MainScreen
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
import java.util.*

class GivolApplication : Application() {
    private val cachedComponents = SparseArray<ActivityComponent>()

    interface ActivityComponent

    override fun onCreate() {
        // Set locale has to be before super.onCreate
        setLocale(baseContext)
        super.onCreate()

        context = this

        startKoin {
            androidContext(this@GivolApplication)
            androidLogger()
            modules(givolApp)
        }

        Timber.plant(Timber.DebugTree())

        FirebaseAuth.getInstance().setLanguageCode("He")

        registerFragmentArguments()
        registerScreens()
        //User.register(this)
    }

    private fun registerFragmentArguments() {
        Arguments.registerSubclass(TransferInfo::class.java)
    }

    private fun registerScreens() {
        Screen.registerSubclass(MainScreen::class.java)
    }

    companion object {
        private var debug: Boolean? = null

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        @JvmStatic
        fun registerActivityComponent(activity: Activity, component: ActivityComponent) {
            val application = activity.applicationContext as GivolApplication
            application.cachedComponents.put(activity.hashCode(), component)
        }

        @JvmStatic
        fun unregisterActivityComponent(activity: Activity) {
            val application = activity.applicationContext as GivolApplication
            application.cachedComponents.remove(activity.hashCode())
        }

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun <T : ActivityComponent> getActivityComponent(activity: Activity): T? {
            val application = activity.applicationContext as GivolApplication
            return application.cachedComponents.get(activity.hashCode()) as T?
        }

        @JvmStatic
        var isDebug: Boolean
            get() = debug!!
            protected set(debug) {
                GivolApplication.debug = debug
            }

        @Suppress("DEPRECATION")
        @JvmStatic
        fun setLocale(context: Context) {
            val locale = Locale("iw", "IL")
            Locale.setDefault(locale)
            val configuration = Configuration()
            configuration.setLocale(locale)
            context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
        }
    }
}
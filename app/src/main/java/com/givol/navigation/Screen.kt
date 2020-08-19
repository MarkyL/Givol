package com.givol.navigation

import android.content.Intent
import com.givol.core.GivolFragment
import com.givol.utils.RuntimeTypeAdapterFactory
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


abstract class Screen protected constructor() {
    private var abstractArguments: String? = null

    // used for Gson serialization
    @Suppress("unused")
    private val type: String = javaClass.simpleName

    protected constructor(arguments: Arguments) : this() {
        abstractArguments = arguments.serialize()
    }

    protected abstract fun create(): GivolFragment

    fun createFragment(): GivolFragment {
        val fragment = create()
        abstractArguments?.let { fragment.setArguments(it) }
        return fragment
    }

    fun serialize(): String {
        val gson = GsonBuilder().registerTypeAdapterFactory(factory).create()
        return gson.toJson(this)
    }

    fun setArguments(arguments: Arguments) {
        abstractArguments = arguments.serialize()
    }

    companion object {
        private val SCREENS = "screens"
        private val factory = RuntimeTypeAdapterFactory.of(Screen::class.java)

        @JvmStatic
        fun <T : Screen> registerSubclass(clazz: Class<T>) {
            factory.registerSubtype(clazz)
        }

        @JvmStatic
        fun <T : Screen> deserialize(json: String): T {
            val gson = GsonBuilder().registerTypeAdapterFactory(factory).create()
            return gson.fromJson<T>(json, TypeToken.get(Screen::class.java).type)
        }

        @JvmStatic
        fun <T : Screen> serialize(intent: Intent, screens: List<T>) {
            val serializedScreens = ArrayList<String>()
            screens.mapTo(serializedScreens) { it.serialize() }
            intent.putStringArrayListExtra(SCREENS, serializedScreens)
        }

        @JvmStatic
        fun deserialize(intent: Intent): List<Screen>? {
            val serializedScreens = intent.getStringArrayListExtra(SCREENS)
            var screens: ArrayList<Screen>? = null
            if (serializedScreens != null) {
                screens = ArrayList()
                serializedScreens.mapTo(screens) { Screen.deserialize(it) }
            }

            return screens
        }

        @JvmStatic
        fun containsScreens(intent: Intent): Boolean =
            intent.extras != null && intent.extras!!.containsKey(SCREENS)


    }
}

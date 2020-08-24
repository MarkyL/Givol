package com.givol.model

import android.content.Context
import android.content.SharedPreferences
import com.givol.utils.StringUtils
import timber.log.Timber

data class User(var name: String? = StringUtils.EMPTY_STRING, val uid: String) {

    constructor() : this(StringUtils.EMPTY_STRING, StringUtils.EMPTY_STRING)

    companion object {

        private var me: User? = null
        private lateinit var preferences: SharedPreferences
        const val CURRENT_USER_FILE_NAME = "current_user"
        private const val NAME_PREFERENCE = "name"
        private const val UID_PREFERENCE = "uid"

        fun create(name: String?, uid: String): User {
            me = User(name, uid)

            me?.let {
                if (name != null)
                    it.setUsername(name)

                it.setUID(uid)
                return it
            }

            return User()
        }

        fun register(context: Context) {
            preferences =
                context.getSharedPreferences(CURRENT_USER_FILE_NAME, Context.MODE_PRIVATE)
        }

        fun me(): User? {
            if (me == null && preferences.getString(UID_PREFERENCE, null) != null) {
                me = User()
                Timber.i("User object is null ! created new User")
            }
            return me
        }

    }

    fun setUsername(phoneNumber: String) {
        preferences.edit().putString(NAME_PREFERENCE, phoneNumber).apply()
    }

    fun getUsername(): String {
        return preferences.getString(NAME_PREFERENCE, StringUtils.EMPTY_STRING).toString()
    }

    fun setUID(fireBaseAuthToken: String) {
        preferences.edit().putString(UID_PREFERENCE, fireBaseAuthToken).apply()
    }

    fun getUID(): String {
        return preferences.getString(UID_PREFERENCE, StringUtils.EMPTY_STRING).toString()
    }

    fun saveBooleanParam(key: String, state: Boolean) {
        preferences.edit().putBoolean(key, state).apply()
    }

    fun getBooleanParam(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

    fun saveLongParam(key: String, value: Long) {
        preferences.edit().putLong(key, value).apply()
    }

    fun getLongParam(key: String): Long {
        return preferences.getLong(key, 0)
    }
}
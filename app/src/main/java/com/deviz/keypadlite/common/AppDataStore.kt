package com.deviz.keypadlite.common

import android.content.Context
import android.content.SharedPreferences

class AppDataStore private constructor(context: Context){
    private val sharedPreferences: SharedPreferences

    companion object {
        private var store: AppDataStore? = null
        private const val filename = "AppDataStore"

        //key 값
        var PIN_KEY = "ENCRYPTED_PIN"

        fun getInstance(context: Context): AppDataStore? {
            if (store == null) {
                store = AppDataStore(context)
            }
            return store
        }
    }

    init {
        sharedPreferences = context.applicationContext.getSharedPreferences(filename, 0)
    }

    fun put(key: String?, value: String?) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.commit()
    }

    operator fun get(key: String?): String? {
        return sharedPreferences.getString(key, "")
    }
    fun clear() {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        editor.commit()
    }
}
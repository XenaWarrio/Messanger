package dx.queen.kotlinfirebasechat.message

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesIsUserRegister {

    private lateinit var prefs: SharedPreferences

    private val APP_SETTINGS = "APP_SETTINGS"
    private val PREFERENCES_NAME = "Registration"

    fun init(context: Context) {
            prefs = context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)

    }
    fun read( value: Boolean): Boolean {
        return prefs.getBoolean(PREFERENCES_NAME, value)
    }


    fun write(value: Boolean) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putBoolean(PREFERENCES_NAME, value)
            commit()
        }
    }


}
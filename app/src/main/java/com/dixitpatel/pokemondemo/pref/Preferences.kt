package com.dixitpatel.pokemondemo.pref

import android.content.Context
import android.content.SharedPreferences
import com.dixitpatel.pokemondemo.application.MyApplication.Companion.instance
import com.dixitpatel.pokemondemo.constant.PREFRENCE_FILE_NAME
import java.util.*

/**
 * Preferences.class : Wrapper of Preferences Data storage.
 */
object Preferences {
    /**
     * @param context - pass context
     * @return SharedPreferences
     */
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFRENCE_FILE_NAME, Context.MODE_PRIVATE)
    }

    /**
     * @param key     - Constant key, will be used for accessing the stored value
     * @param val     - String value to be stored
     */
    fun setPreference(key: String?, `val`: String?) {
        val settings = getSharedPreferences(
            instance
        )
        val editor = settings.edit()
        editor.putString(key, `val`)
        editor.apply()
    }

    /**
     * @param key     - Constant key, will be used for accessing the stored value
     * @param val     - String value to be stored
     */
    fun setPreferenceFloat(key: String?, `val`: Float) {
        val settings = getSharedPreferences(
            instance
        )
        val editor = settings.edit()
        editor.putFloat(key, `val`)
        editor.apply()
    }

    /**
     * @param key
     * @param val
     */
    fun setPreference(key: String?, `val`: Boolean) {
        val settings = getSharedPreferences(
            instance
        )
        val editor = settings.edit()
        editor.putBoolean(key, `val`)
        editor.apply()
    }

    /**
     * @param key
     * @param val
     */
    fun setPreferenceInt(key: String?, `val`: Int) {
        val settings = getSharedPreferences(
            instance
        )
        val editor = settings.edit()
        editor.putInt(key, `val`)
        editor.apply()
    }

    /**
     * Add preferences
     *
     * @param key     - Constant key, will be used for accessing the stored value
     * @param val     - long value to be stored, mostly used to store FB Session value
     */
    fun setPreferenceLong(key: String?, `val`: Long) {
        val settings = getSharedPreferences(
            instance
        )
        val editor = settings.edit()
        editor.putLong(key, `val`)
        editor.apply()
    }

    /**
     * Add preferences
     *
     * @param key     - Constant key, will be used for accessing the stored value
     * @param array<String> val    - ArrayList<String> value to be stored, mostly used to store FB Session value
    </String></String> */
    fun setPreferenceArray(key: String, array: ArrayList<String?>): Boolean {
        val prefs = getSharedPreferences(
            instance
        )
        val editor = prefs.edit()
        editor.putInt(key + "_size", array.size)
        for (i in array.indices) editor.putString(key + "_" + i, array[i])
        return editor.commit()
    }

    fun clearPreferenceArray(key: String) {
        val settings = getSharedPreferences(
            instance
        )
        if (getPreferenceArray(key).size > 0) {
            for (element in getPreferenceArray(key)) {
                if (findPrefrenceKey(element) != null && settings.contains(findPrefrenceKey(element))) {
                    val editor = settings.edit()
                    editor.remove(findPrefrenceKey(element))
                    editor.apply()
                }
            }
        }
    }

    fun findPrefrenceKey(value: String?): String? {
        val settings = getSharedPreferences(
            instance
        )
        val editor = settings.all
        for ((key, value1) in editor) {
            if (value == value1) {
                return key
            }
        }
        return null // not found
    }

    /**
     * Remove preference key
     *
     * @param key     - the key which you stored before
     */
    fun removePreference(key: String?) {
        val settings = getSharedPreferences(
            instance
        )
        val editor = settings.edit()
        editor.remove(key)
        editor.apply()
    }

    /**
     * Get preference value by passing related key
     *
     * @param key     - key value used when adding preference
     * @return - String value
     */
    fun getPreferenceString(key: String?): String? {
        val prefs = getSharedPreferences(
            instance
        )
        return prefs.getString(key, "")
    }

    /**
     * Get preference ArrayList<String> value by passing related key
     *
     * @param key     - key value used when adding preference
     * @return - ArrayList<String> value
    </String></String> */
    fun getPreferenceArray(key: String): ArrayList<String?> {
        val prefs = getSharedPreferences(
            instance
        )
        val size = prefs.getInt(key + "_size", 0)
        val array = ArrayList<String?>(size)
        for (i in 0 until size) array.add(prefs.getString(key + "_" + i, null))
        return array
    }

    /**
     * Get preference value by passing related key
     *
     * @param key     - key value used when adding preference
     * @return - long value
     */
    fun getPreferenceLong(key: String?): Long {
        val prefs = getSharedPreferences(
            instance
        )
        return prefs.getLong(key, 0)
    }

    fun getPreferenceBoolean(key: String?): Boolean {
        val prefs = getSharedPreferences(
            instance
        )
        return prefs.getBoolean(key, false)
    }

    fun getPreferenceInt(key: String?): Int {
        val prefs = getSharedPreferences(
            instance
        )
        return prefs.getInt(key, 0)
    }

    fun getPreferenceFloat(key: String?): Float {
        val prefs = getSharedPreferences(
            instance
        )
        return prefs.getFloat(key, 0f)
    }

    /**
     * Clear all stored  preferences
     *
     */
    fun removeAllPreference() {
        val settings = getSharedPreferences(
            instance
        )
        val editor = settings.edit()
        editor.clear()
        editor.apply()
    }

    /**
     * Clear all stored  preferences
     *
     */
    val allPreference: String
        get() {
            val settings = getSharedPreferences(
                instance
            )
            val editor = settings.all
            var text = ""
            try {
                for ((key, value1) in editor) {
                    val value = value1!!
                    // do stuff
                    text += "\t$key = $value\t"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return text
        }
}
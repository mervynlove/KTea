

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

/**
 * Create by MengWei at 2018/7/13
 */

@SuppressLint("ApplySharedPref")
inline fun SharedPreferences.edit(
        commit: Boolean = false,
        action: SharedPreferences.Editor.() -> Unit
) {
    val editor = edit()
    action(editor)
    if (commit) {
        editor.commit()
    } else {
        editor.apply()
    }
}

private val DEFAULT_SHARED_PREFERENCES_FILE_NAME = "default_shared_preferences_file"

fun Context.sharedPreferences(fileName: String, mode: Int, function: (SharedPreferences.Editor.() -> Unit)) {
    val sharedPreferencesEditor = getSharedPreferences(fileName, mode).edit()
    sharedPreferencesEditor.function()
    sharedPreferencesEditor.apply()
    sharedPreferencesEditor.commit()
}

fun Context.defaultSharedPreferences(function: (SharedPreferences.Editor.() -> Unit)) {
    val sharedPreferencesEditor = getSharedPreferences(DEFAULT_SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE).edit()
    sharedPreferencesEditor.function()
    sharedPreferencesEditor.apply()
    sharedPreferencesEditor.commit()
}

fun <T> Context.getFromSharedPreferences(fileName: String, key: String, default: T): T? where T : Any {
    val sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE)

    val value = when (default) {
        is Int -> sharedPreferences.getInt(key, default)
        is String -> sharedPreferences.getString(key, default)
        is Boolean -> sharedPreferences.getBoolean(key, default)
        is Float -> sharedPreferences.getFloat(key, default)
        is Long -> sharedPreferences.getLong(key, default)
        else -> null
    }

    return value as T
}

fun <T> Context.getFromDefaultSharedPreferences(key: String, default: T): T? where T : Any {
    val sharedPreferences = getSharedPreferences(DEFAULT_SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)

    val value = when (default) {
        is Int -> sharedPreferences.getInt(key, default)
        is String -> sharedPreferences.getString(key, default)
        is Boolean -> sharedPreferences.getBoolean(key, default)
        is Float -> sharedPreferences.getFloat(key, default)
        is Long -> sharedPreferences.getLong(key, default)
        else -> null
    }

    return value as T
}
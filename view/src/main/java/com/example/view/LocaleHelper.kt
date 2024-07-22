package com.example.view

import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import com.example.view.constant.Constants
import java.util.Locale

object LocaleHelper {
    private const val KEY_SELECTED_LANGUAGE = "KEY_SELECTED_LANGUAGE"
    private const val KEY_FIRST_LAUNCH_APP = "KEY_FIRST_LAUNCH_APP"

    fun onAttach(context: Context): Context {
        val lang = getPersistedData(context)
        return setLocale(context, lang)
    }

    fun getLanguage(context: Context): String {
        return getPersistedData(context)
    }

    fun setLocale(
        context: Context,
        language: String,
    ): Context {
        persist(context, language)
        return updateResources(context)
    }

    fun setFirstTimeLaunch(
        context: Context,
        isFirstTime: Boolean,
    ) {
        persistFirstTimeLaunch(context, isFirstTime)
    }

    fun isFirstTimeLaunch(context: Context): Boolean {
        return getPersistedFirstTimeLaunch(context)
    }

    fun updateResources(context: Context): Context {
        val locale = Locale(getPersistedData(context))
        Locale.setDefault(locale)
        val resource = context.resources
        val configuration = resource.configuration
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
            context.createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
            configuration.setLayoutDirection(locale)
            resource.updateConfiguration(configuration, resource.displayMetrics)
            context
        }
    }

    private fun getPersistedData(context: Context): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(KEY_SELECTED_LANGUAGE, Locale.getDefault().language) ?: Constants.EN_LANGUAGE
    }

    private fun persist(
        context: Context,
        language: String?,
    ) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(KEY_SELECTED_LANGUAGE, language)
        editor.apply()
    }

    private fun persistFirstTimeLaunch(
        context: Context,
        isFirstTime: Boolean,
    ) {
        val preference = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preference.edit()
        editor.apply {
            putBoolean(KEY_FIRST_LAUNCH_APP, isFirstTime)
            apply()
        }
    }

    private fun getPersistedFirstTimeLaunch(context: Context): Boolean {
        val preference = PreferenceManager.getDefaultSharedPreferences(context)
        return preference.getBoolean(KEY_FIRST_LAUNCH_APP, true)
    }
}

package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    companion object{
        const val NIGHT_MODE = "night_mode"
        var darkTheme = false
    }


    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(NIGHT_MODE, MODE_PRIVATE)
        darkTheme = sharedPrefs.getString(NIGHT_MODE, "false").toBoolean()

        (applicationContext as App).switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    companion object{
        var darkTheme = false
    }


    override fun onCreate() {
        super.onCreate()
        Creator.initAppContext(applicationContext)//передаем контекст приложения в creator

        //Используем метод из интерактора
        darkTheme = Creator.provideNightModeInteractor().getNightMode().isNight

        switchTheme(darkTheme)
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
package com.example.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator

class App : Application() {

    companion object{
        var darkTheme = false
    }


    override fun onCreate() {
        super.onCreate()
        Creator.initAppContext(applicationContext)//передаем контекст приложения в creator

        //Используем метод из интерактора
        darkTheme = Creator.provideNightModeInteractor().getSettings().isNightMode

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
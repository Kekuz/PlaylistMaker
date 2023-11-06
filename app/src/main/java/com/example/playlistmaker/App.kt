package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.repository.NightModeRepositoryImpl
import com.example.playlistmaker.data.storage.SharedPrefNightModeStorage
import com.example.playlistmaker.domain.api.interactor.NightModeInteractor
import com.example.playlistmaker.domain.api.repository.NightModeRepository
import com.example.playlistmaker.domain.impl.NightModeInteractorImpl

class App : Application() {

    companion object{
        var darkTheme = false
    }


    override fun onCreate() {
        super.onCreate()
        Creator.initAppContext(applicationContext)//передаем контекст приложения в creator

        //Используем метод из интерактора
        darkTheme = Creator.provideNightModeInteractor().getNightMode().isNight

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
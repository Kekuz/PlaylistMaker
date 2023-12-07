package com.example.playlistmaker.app

import android.app.Application
import com.example.playlistmaker.creator.Creator

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        //Передаем контекст приложения в creator
        Creator.initAppContext(applicationContext)
        //Меняем тему приложения со старта
        Creator.provideSettingsInteractor().changeTheme()
    }
}
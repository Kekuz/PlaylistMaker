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
    //Как черт создаю дважды, потому что тут надо передавать контекст, а без DI я не знаю как это делать
    //Просто статик класс с контекстом не катит
    private fun getNightModeRepository(): NightModeRepository {
        return NightModeRepositoryImpl(SharedPrefNightModeStorage(applicationContext))
    }

    fun provideNightModeInteractor(): NightModeInteractor {
        return NightModeInteractorImpl(getNightModeRepository())
    }


    override fun onCreate() {
        super.onCreate()

        //Используем метод из интерактора
        darkTheme = provideNightModeInteractor().getNightMode().isNight

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
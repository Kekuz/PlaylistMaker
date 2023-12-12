package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.SearchHistoryStorage
import com.example.playlistmaker.data.search.network.ITunesAPI
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.storage.SharedPrefsSearchHistoryStorage
import com.example.playlistmaker.data.settings.SettingsStorage
import com.example.playlistmaker.data.settings.storage.SharedPrefSettingsStorage
import com.example.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.example.playlistmaker.domain.sharing.api.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val IS_NIGHT = "is_night"

val dataModule = module {

    single<ITunesAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesAPI::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(androidContext(), get())
    }

    single<SearchHistoryStorage> {
        SharedPrefsSearchHistoryStorage(androidContext())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    single<SettingsStorage> {
        SharedPrefSettingsStorage(get())
    }

    single { androidContext().getSharedPreferences(IS_NIGHT, Context.MODE_PRIVATE) }

    factory { android.media.MediaPlayer() }

}




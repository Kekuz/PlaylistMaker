package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.playlistmaker.data.favorites.DatabaseClient
import com.example.playlistmaker.data.favorites.database.RoomDatabaseClient
import com.example.playlistmaker.data.favorites.database.TrackDatabase
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
        Retrofit.Builder().baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ITunesAPI::class.java)
    }

    single<TrackDatabase> {
        Room.databaseBuilder(
            androidContext(), TrackDatabase::class.java, "track-database"
        ).build()
    }

    single<DatabaseClient> {
        RoomDatabaseClient(get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(androidContext(), get())
    }

    single<SearchHistoryStorage> {
        SharedPrefsSearchHistoryStorage(androidContext(), get())
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




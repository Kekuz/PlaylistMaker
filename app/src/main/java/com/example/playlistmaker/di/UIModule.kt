package com.example.playlistmaker.di

import com.example.playlistmaker.data.search.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.domain.search.api.repository.SearchHistoryRepository
import com.example.playlistmaker.ui.util.Debounce
import org.koin.dsl.module

val UIModuleModule = module {
    single<Debounce> {
        Debounce()
    }
}
package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.domain.sharing.api.ExternalNavigator
import com.example.playlistmaker.domain.sharing.api.interactor.SharingInteractor
import com.example.playlistmaker.domain.sharing.models.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return "https://practicum.yandex.ru/android-developer/?from=catalog"
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            "Сообщение разработчикам и разработчицам приложения Playlist Maker",
            "Спасибо разработчикам и разработчицам за крутое приложение!",
            "anton.edlenko@yandex.ru"
        )
    }

    private fun getTermsLink(): String {
        return "https://yandex.ru/legal/practicum_offer/"
    }
}
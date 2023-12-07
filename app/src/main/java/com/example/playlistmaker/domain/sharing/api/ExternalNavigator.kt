package com.example.playlistmaker.domain.sharing.api

import com.example.playlistmaker.domain.sharing.models.EmailData

interface ExternalNavigator {
    fun shareLink(shareAppLink: String)
    fun openLink(termsLink: String)
    fun openEmail(supportEmailData: EmailData)
}
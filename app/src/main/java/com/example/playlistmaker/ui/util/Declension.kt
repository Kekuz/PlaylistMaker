package com.example.playlistmaker.ui.util

object Declension {
    fun incline(count: Int, one: String, two: String, five: String): String =
        when {
            (count % 100) in 5..20 -> five
            (count % 10) == 1 -> one
            count in 2..4 -> two
            else -> five
        }

}